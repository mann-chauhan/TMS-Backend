package com.tms.tms_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    )
            throws ServletException, IOException {

        // =====================================
        // GET AUTH HEADER
        // =====================================

        String authHeader =
                request.getHeader("Authorization");

        // =====================================
        // CHECK TOKEN EXISTS
        // =====================================

        if (
                authHeader == null
                        ||
                        !authHeader.startsWith("Bearer ")
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        // =====================================
// EXTRACT TOKEN
// =====================================

        String token =
                authHeader.substring(7);

// =====================================
// EXTRACT EMAIL + ROLE
// =====================================

        String email;
        String role;

        try {

            email =
                    jwtService.extractEmail(token);

            role =
                    jwtService.extractRole(token);

        } catch (Exception e) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

// =====================================
// VALIDATE TOKEN
// =====================================

        if (
                !jwtService.isTokenValid(
                        token,
                        email
                )
        ) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

// =====================================
// CREATE AUTH OBJECT
// =====================================

        UsernamePasswordAuthenticationToken authToken =

                new UsernamePasswordAuthenticationToken(

                        email,

                        null,

                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role
                                )
                        )
                );

// =====================================
// SET AUTHENTICATION
// =====================================

        SecurityContextHolder
                .getContext()
                .setAuthentication(authToken);

// =====================================
// CONTINUE REQUEST
// =====================================

        filterChain.doFilter(
                request,
                response
        );
    }
}