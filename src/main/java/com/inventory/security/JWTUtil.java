package com.inventory.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

public class JWTUtil {

    private final Key secretKey;
    private final int tokenValidity;

    // Constructor that generates a secure key
    public JWTUtil(String secretKey, int tokenValidity) {
        // Use the Keys class to generate a secure key based on the secret
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenValidity = tokenValidity;
    }

    /**
     * Generates a JWT token for the given username.
     * The token will contain a subject (username), issue date, and expiration date,
     * and will be signed using the HS256 algorithm.
     *
     * @param username the username to include in the token
     * @return the generated JWT token as a string
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Updated to use Key object
                .compact();
    }

    /**
     * Validates the JWT token by checking its claims and ensuring it is not expired.
     *
     * @param token the JWT token to validate
     * @param username the username to compare with the token's subject
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean isTokenValid(String token, String username) {
        Optional<Claims> claims = extractClaimsFromToken(token);
        return claims.map(c -> username.equals(c.getSubject()) && !isTokenExpired(c))
                .orElse(false);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token the JWT token to extract claims from
     * @return an Optional containing the Claims, or empty if token parsing fails
     */
    private Optional<Claims> extractClaimsFromToken(String token) {
        try {
            return Optional.of(Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody());
        } catch (Exception e) {
            // In case of parsing issues, return an empty Optional
            return Optional.empty();
        }
    }

    /**
     * Checks if the token is expired.
     *
     * @param claims the claims extracted from the token
     * @return true if the token has expired, false otherwise
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


}
