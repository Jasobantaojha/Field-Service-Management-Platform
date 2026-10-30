package com.KeyStone.FieldServiceProject.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenBlockService tokenBlockService;

    public JwtAuthenticationFilter(
            JWTUtil jwtUtil,
            CustomUserDetailsService customUserDetailsService,
            TokenBlockService tokenBlockService) {

        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenBlockService = tokenBlockService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // Continue normally when no Bearer token is provided.
        if (!StringUtils.hasText(header)
                || !header.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        // Reject tokens that have been blacklisted.
        if (tokenBlockService.isblockListToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"error\":\"Token has been logged out\"}"
            );
            return;
        }

        // Authenticate the user if the token is valid.
        if (jwtUtil.validateToken(token)
                && SecurityContextHolder.getContext()
                        .getAuthentication() == null) {

            String userEmail = jwtUtil.getUserEmail(token);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(userEmail);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}