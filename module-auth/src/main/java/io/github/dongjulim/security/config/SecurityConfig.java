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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String AUTHENTICATION_URL = "/api/v2/login";
    private static final String API_ROOT_URL = "/api/v2/**";
    private static final String[] ADMIN_ROLES = { Role.ADMIN.name(), Role.MASTER.name() };

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenIssueProvider jwtTokenIssueProvider;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final ObjectMapper objectMapper;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        var builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.authenticationProvider(jwtAuthenticationProvider);
        builder.authenticationProvider(jwtTokenIssueProvider);
        return builder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
        http
            .csrf().disable()
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeRequests(auth -> auth
                .antMatchers(HttpMethod.POST,   "/api/v2/deliveries").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PATCH,  "/api/v2/deliveries/*/status").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.DELETE, "/api/v2/deliveries/*/return").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.POST,   "/api/v2/notice").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PUT,    "/api/v2/notice/*").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.DELETE, "/api/v2/notice/*").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.POST,   "/api/v2/notifications").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.POST,   "/api/v2/product/categories").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PUT,    "/api/v2/product/categories/*").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.DELETE, "/api/v2/product/categories/*").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PATCH,  "/api/v2/product/*/stock").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.POST,   "/api/v2/coupons").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.POST,   "/api/v2/coupons/*/users/*").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PATCH,  "/api/v2/user/*/role").hasAnyRole(ADMIN_ROLES)
                .antMatchers(HttpMethod.PATCH,  "/api/v2/user/*/grade").hasAnyRole(ADMIN_ROLES)
            )
            .addFilterBefore(jwtTokenIssueFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(
                jwtTokenAuthenticationFilter(List.of(
                    new AntPathRequestMatcher(AUTHENTICATION_URL),
                    new AntPathRequestMatcher("/api/v2/notice", "GET"),
                    new AntPathRequestMatcher("/api/v2/notice/*", "GET"),
                    new AntPathRequestMatcher("/api/v2/user", "POST")
                ), authenticationManager),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private JwtTokenIssueFilter jwtTokenIssueFilter(AuthenticationManager authenticationManager) {
        var filter = new JwtTokenIssueFilter(AUTHENTICATION_URL, objectMapper, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(List<RequestMatcher> pathsToSkip,
                                                                      AuthenticationManager authenticationManager) {
        var filter = new JwtTokenAuthenticationFilter(new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL), failureHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}
