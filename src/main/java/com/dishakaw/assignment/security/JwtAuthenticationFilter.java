package com.dishakaw.assignment.security;

import com.dishakaw.assignment.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

    // Extract the JWT from the Authorization header
    String token = getJwtFromRequest(request);

    if (token != null) {
        // Extract username and roles from the token
        String username = jwtUtil.extractUsername(token);
        List<SimpleGrantedAuthority> authorities = extractRolesFromToken(token);

        // Validate token using username and token
        if (jwtUtil.validateToken(token)) {
            // Create an authentication object with roles/authorities
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // Reject invalid token with 403 Forbidden status
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            return; // Stop further processing
        }
    }

    filterChain.doFilter(request, response);  // Proceed with the filter chain
}

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Extract roles from the token and create authorities
    private List<SimpleGrantedAuthority> extractRolesFromToken(String token) {
        List<String> roles = jwtUtil.extractRoles(token); // Assuming extractRoles method exists in JwtUtil
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        return authorities;
    }
}
