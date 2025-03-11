package com.example.shopease.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public AuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Incoming Request URI: " + request.getRequestURI());

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // Extract token
        final String username = jwtUtil.extractUsername(jwt); // Extract username

        // Ensure user is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<String> roles = jwtUtil.extractRoles(jwt); // Extract roles from JWT
            log.info("User has roles: {}", roles);

            if (jwtUtil.isTokenValid(jwt, username)) {
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role-> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());


                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
