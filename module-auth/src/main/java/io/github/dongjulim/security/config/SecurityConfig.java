package io.github.dongjulim.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dongjulim.domain.user.enums.Role;
import io.github.dongjulim.security.SkipPathRequestMatcher;
import io.github.dongjulim.security.filter.JwtTokenAuthenticationFilter;
import io.github.dongjulim.security.filter.JwtTokenIssueFilter;
import io.github.dongjulim.security.provider.JwtAuthenticationProvider;
import io.github.dongjulim.security.provider.JwtTokenIssueProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final String AUTHENTICATION_URL = "/api/v2/login";
    private final String API_ROOT_URL = "/api/v2/**";
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenIssueProvider jwtTokenIssueProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(jwtTokenIssueProvider);

        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http.authorizeRequests().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/").hasAnyRole(Role.ADMIN.name(),Role.MASTER.name())
                .and()
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenIssueFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(
                        jwtTokenAuthenticationFilter(List.of(AUTHENTICATION_URL), authenticationManager),
                        UsernamePasswordAuthenticationFilter.class
                )
        ;

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5174")); // 프론트엔드 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 자격 증명 허용 (매우 중요)
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config); // 모든 경로에 CORS 적용
        return source;
    }

    private JwtTokenIssueFilter jwtTokenIssueFilter(AuthenticationManager authenticationManager) {
        var filter = new JwtTokenIssueFilter(AUTHENTICATION_URL, objectMapper, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(List<String> pathsToSkip,
                                                                      AuthenticationManager authenticationManager) {
        var matcher = new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL);

        var filter = new JwtTokenAuthenticationFilter(matcher, failureHandler);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }
}
