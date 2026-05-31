package com.backend.sentinel.controller;

import com.backend.sentinel.dto.AuthResponse;
import com.backend.sentinel.dto.LoginRequest;
import com.backend.sentinel.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationController Unit Tests")
class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private LoginRequest validRequest;
    private static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.test.token";

    @BeforeEach
    void setUp() {
        validRequest = new LoginRequest("testuser", "password123");
    }

    @Test
    @DisplayName("Valid credentials return 200 with token")
    void login_validCredentials_returns200WithToken() {
        when(jwtService.generateToken("testuser")).thenReturn(TEST_TOKEN);

        ResponseEntity<AuthResponse> response = authenticationController.login(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TEST_TOKEN, response.getBody().token());
    }

    @Test
    @DisplayName("Valid credentials trigger authentication and token generation")
    void login_validCredentials_authenticatesAndGeneratesToken() {
        when(jwtService.generateToken("testuser")).thenReturn(TEST_TOKEN);

        authenticationController.login(validRequest);

        // Both must be called: auth first, then token generation
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, times(1)).generateToken("testuser");
    }

    @Test
    @DisplayName("Bad credentials return 401 with empty body")
    void login_badCredentials_returns401() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        ResponseEntity<AuthResponse> response = authenticationController.login(validRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Token is never generated on failed authentication")
    void login_badCredentials_neverGeneratesToken() {
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(any());

        authenticationController.login(validRequest);

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Authentication is called with the correct username and password")
    void login_passesCorrectCredentialsToAuthManager() {
        when(jwtService.generateToken(any())).thenReturn(TEST_TOKEN);

        authenticationController.login(validRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testuser", "password123")
        );
    }
}
