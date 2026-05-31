package com.backend.sentinel.controller;

import com.backend.sentinel.dto.RegisterRequest;
import com.backend.sentinel.exception.UsernameAlreadyExistsException;
import com.backend.sentinel.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrationController Unit Tests")
class RegistrationControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RegisterRequest("newuser", "user@example.com", "securepassword");
    }

    @Test
    @DisplayName("Valid registration returns 200 with success message")
    void register_validRequest_returns200WithMessage() {
        ResponseEntity<String> response = registrationController.registerUser(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    @DisplayName("Registration delegates to service exactly once")
    void register_validRequest_callsServiceOnce() {
        registrationController.registerUser(validRequest);

        verify(registrationService, times(1)).saveRegisterService(validRequest);
    }

    @Test
    @DisplayName("UsernameAlreadyExistsException propagates to GlobalExceptionHandler")
    void register_duplicateUsername_throwsException() {
        // The controller lets the exception bubble up; GlobalExceptionHandler maps it to 409.
        // This test verifies the controller does not swallow or wrap the exception.
        doThrow(new UsernameAlreadyExistsException("Username is already in use"))
                .when(registrationService).saveRegisterService(any());

        assertThrows(UsernameAlreadyExistsException.class,
                () -> registrationController.registerUser(validRequest));
    }
}
