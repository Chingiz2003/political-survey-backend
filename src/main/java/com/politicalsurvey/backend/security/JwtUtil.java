package com.politicalsurvey.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 86400000; // 24 часа

    public String generateToken(Integer citizenId) {
        return Jwts.builder()
                .setSubject(citizenId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public Integer validateTokenAndGetId(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return Integer.parseInt(claims.getBody().getSubject());
        } catch (Exception e) {
            System.out.println("JWT validation error: " + e.getMessage());
            throw e;
        }
    }
}