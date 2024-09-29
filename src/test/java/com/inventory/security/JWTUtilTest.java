package com.inventory.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JWTUtilTest {

    public static final String USERNAME = "valid user";
    private final String SECRET_KEY = "mySecretKeyThatIsAtLeast32CharLong";
    private final int TOKEN_VALID_ONE_HOUR = 3600000; // 1 hour in milliseconds
    private static final int TOKEN_VALID_ONE_MILLISECOND = 1; // 1 millisecond validity

    private final JWTUtil jwtUtil = new JWTUtil(SECRET_KEY, TOKEN_VALID_ONE_HOUR);

    @Test
    public void shouldGenerateAndValidateJWTToken() {
        String token = jwtUtil.generateToken(USERNAME);

        assertNotNull(token, "Generated token should not be null.");
        assertTrue(jwtUtil.isTokenValid(token, USERNAME), "Token should be valid for the same username.");
    }

    @Test
    public void shouldInvalidateTokenWithWrongUsername() {
        String token = jwtUtil.generateToken(USERNAME);

        assertFalse(jwtUtil.isTokenValid(token, "wrong user"),
                "Token should be invalid for an incorrect username.");
    }

    @Test
    public void shouldInvalidateExpiredToken() throws InterruptedException {
        JWTUtil shortLivedJwtUtil = new JWTUtil(SECRET_KEY, TOKEN_VALID_ONE_MILLISECOND);

        String token = shortLivedJwtUtil.generateToken(USERNAME);
        Thread.sleep(200);  // Simulate a delay that exceeds the token's expiration time.

        // Assert: Verify that the token is no longer valid after expiration.
        assertFalse(shortLivedJwtUtil.isTokenValid(token, USERNAME),
                "Token should be invalidated after expiration.");
    }

    @Test
    public void shouldIsTokenValidAndVerifyClaims() {
        String token = jwtUtil.generateToken(USERNAME);
        boolean isValid = jwtUtil.isTokenValid(token, USERNAME);

        assertTrue(isValid, "Token should be valid for the correct username.");
    }

    @Test
    public void shouldInvalidateNullOrEmptyToken() {
        // Test with a null token
        assertFalse(jwtUtil.isTokenValid(null, USERNAME),
                "Null token should be invalid.");

        // Test with an empty token
        assertFalse(jwtUtil.isTokenValid("", USERNAME),
                "Empty token should be invalid.");
    }

    @Test
    public void shouldInvalidateTamperedToken() {
        String validToken = jwtUtil.generateToken(USERNAME);

        // Tamper with the token by modifying its content
        String tamperedToken = validToken.substring(0, validToken.length() - 2) + "XX";

        assertFalse(jwtUtil.isTokenValid(tamperedToken, USERNAME),
                "Tampered token should be invalid.");
    }

    @Test
    public void shouldInvalidateTokenWithWrongSigningKey() {
        // Generate token with original key
        String token = jwtUtil.generateToken(USERNAME);

        // Create a new JWTUtil with a different key
        JWTUtil wrongKeyJwtUtil = new JWTUtil("differentSecretKeyThatIsAlso32CharLong", TOKEN_VALID_ONE_HOUR);

        // Token signed with different key should be invalid
        assertFalse(wrongKeyJwtUtil.isTokenValid(token, USERNAME),
                "Token should be invalid with a different signing key.");
    }

}