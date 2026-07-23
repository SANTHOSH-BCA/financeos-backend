package com.financeos.financeosbackend.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    @DisplayName("Should generate JWT token")
    void shouldGenerateToken() {

        String token = jwtService.generateToken("test@example.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("Should extract email from token")
    void shouldExtractEmail() {

        String token = jwtService.generateToken("test@example.com");

        String email = jwtService.extractEmail(token);

        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("Should validate valid token")
    void shouldValidateValidToken() {

        String token = jwtService.generateToken("test@example.com");

        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Should reject invalid token")
    void shouldRejectInvalidToken() {

        assertFalse(jwtService.isTokenValid("invalid-token"));
    }

    @Test
    @DisplayName("Should reject malformed token")
    void shouldRejectMalformedToken() {

        String malformedToken = "abc.def.ghi";

        assertFalse(jwtService.isTokenValid(malformedToken));
    }
}