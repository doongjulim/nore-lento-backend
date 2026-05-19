package io.github.dongjulim.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,   "/api/v2/deliveries").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.PATCH,  "/api/v2/deliveries/*/status").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.DELETE, "/api/v2/deliveries/*/return").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.POST,   "/api/v2/notice").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.PUT,    "/api/v2/notice/*").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.DELETE, "/api/v2/notice/*").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.POST,   "/api/v2/notifications").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.POST,   "/api/v2/product/categories").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.PUT,    "/api/v2/product/categories/*").hasAnyRole("ADMIN", "MASTER")
                .antMatchers(HttpMethod.DELETE, "/api/v2/product/categories/*").hasAnyRole("ADMIN", "MASTER")
                .anyRequest().authenticated();
        return http.build();
    }
}
