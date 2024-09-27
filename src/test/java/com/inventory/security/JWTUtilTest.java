package com.inventory.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JWTUtilTest {

    // Ensure the secret key is at least 32 characters long for HS256
    private final String secretKey = "mySecretKeyThatIsAtLeast32CharLong";
    private final int tokenValidity = 3600000; // 1 hour in milliseconds

    private JWTUtil jwtUtil = new JWTUtil(secretKey, tokenValidity);

    @Test
    public void shouldGenerateValidJWTToken() {
        // Arrange
        String username = "testuser";

        // Act
        String token = jwtUtil.generateToken(username);

        // Assert
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token, username));
    }

}
