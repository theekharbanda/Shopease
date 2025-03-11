package com.example.shopease.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Component
public class JwtUtil {
    private final SecretKey signingKey;

    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        // using HMAC-SHA256 algo
        this.signingKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
    }

    public boolean isTokenValid(String token, String userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails) && !isTokenExpired(token));
    }

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles) // Storing roles in token
                .issuedAt(new Date())
                // 1-hour expiry
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(signingKey)
                .compact();
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
