package com.backend.Backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {
    private static final SecretKey secret_key = Jwts.SIG.HS256.key().build();

    public static String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .signWith(secret_key)
                .compact();
    }

    public static Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secret_key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
