package com.example.kanban.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        // get Authorization header
        String authHeader = request.getHeader("Authorization");
        // looks like: "Bearer eyJhbGc..."

        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            // ↑ remove "Bearer " prefix

            if (jwtUtil.validateToken(token)) {
                email = jwtUtil.getEmailFromToken(token);
            }
        }

        // if valid token and not already authenticated
        if (email != null &&
            SecurityContextHolder.getContext()
                                 .getAuthentication() == null) {

            UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                    email, null, new ArrayList<>()
                );

            auth.setDetails(
                new WebAuthenticationDetailsSource()
                    .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                                 .setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}