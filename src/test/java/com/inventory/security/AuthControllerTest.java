package com.inventory.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTUtil jwtUtil;

    @Autowired
    private AuthController authController;

    @BeforeEach
    public void setup() {
        when(jwtUtil.generateToken(any(String.class))).thenReturn("mocked-jwt-token");
    }

    // Test-specific configuration class
    @TestConfiguration
    static class TestConfig {

        @Bean
        public String jwtSecret() {
            return "test-secret-key"; // Default test key
        }
    }

    @Test
    public void loginWithValidCredentialsShouldReturnJWT() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        // When
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                // Then
                .andExpect(status().isOk()) // Expect 200 OK status
                .andExpect(jsonPath("$.token").isNotEmpty()); // Expect JWT token in response
    }

    @Test
    public void loginWithInvalidCredentialsShouldReturnUnauthorized() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        // When
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                // Then
                .andExpect(status().isUnauthorized());
    }


    // Helper method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
