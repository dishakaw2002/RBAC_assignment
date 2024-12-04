package com.dishakaw.assignment.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    private final Key secretKey = Keys.hmacShaKeyFor("your-very-strong-secret-key-for-hs256-32!".getBytes());

    // Generate a JWT token that includes roles
    public String generateToken(String username, Set<String> roles) {
        return Jwts.builder()
                .claim("username", username)
                .claim("roles", roles)  // Add roles to the JWT token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour expiration
                .signWith(secretKey)
                .compact();
    }

    // Validate the token (same as before)
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // Extract username from the JWT token
    public String extractUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    // Extract roles from the JWT token
    public List<String> extractRoles(String token) {
        return extractClaims(token).get("roles", List.class); // Return roles as List
    }

    // Extract claims from the token
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or tampered token", e);
        }
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
