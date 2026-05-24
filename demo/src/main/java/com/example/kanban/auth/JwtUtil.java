package com.example.kanban.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // get signing key
    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // generate token
    public String generateToken(String email) {
        return Jwts.builder()
            .subject(email)
            // ↑ 0.12.x uses .subject() not .setSubject()
            .issuedAt(new Date())
            .expiration(new Date(
                System.currentTimeMillis() + expiration
            ))
            .signWith(getKey())
            // ↑ 0.12.x no need to pass algorithm
            .compact();
    }

    // get email from token
    public String getEmailFromToken(String token) {
        return Jwts.parser()
            // ↑ 0.12.x uses .parser() not .parserBuilder()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            // ↑ 0.12.x uses .parseSignedClaims()
            .getPayload()
            .getSubject();
    }

    // validate token
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}