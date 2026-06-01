package com.tms.tms_backend.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                .cors(cors -> {})

                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // Users management (admin-only in this project)
                        .requestMatchers("/api/users/**")
                        .hasRole("ADMIN")

                        // Travel requests route protection (controllers live under /api/requests/**)
                        .requestMatchers(HttpMethod.POST, "/api/requests")
                        .hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/requests/employee/**")
                        .hasRole("EMPLOYEE")
                        .requestMatchers("/api/requests/cancel/**")
                        .hasRole("EMPLOYEE")

                        .requestMatchers("/api/requests/manager/**")
                        .hasRole("MANAGER")
                        .requestMatchers("/api/requests/manager")
                        .hasRole("MANAGER")
                        .requestMatchers("/api/requests/approve/**")
                        .hasRole("MANAGER")
                        .requestMatchers("/api/requests/reject/**")
                        .hasRole("MANAGER")

                        .requestMatchers("/api/requests/finance/**")
                        .hasRole("FINANCE")

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
