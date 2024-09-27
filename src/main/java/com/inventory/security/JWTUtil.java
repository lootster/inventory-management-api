package com.inventory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JWTUtil {

    private final Key secretKey;
    private final int tokenValidity;

    // Constructor that generates a secure key
    public JWTUtil(String secretKey, int tokenValidity) {
        // Use the Keys class to generate a secure key based on the secret
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenValidity = tokenValidity;
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Updated to use Key object
                .compact();
    }

    public boolean validateToken(String token, String username) {
        Claims claims = extractAllClaims(token);
        String extractedUsername = claims.getSubject();
        return (username.equals(extractedUsername) && !isTokenExpired(claims));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
