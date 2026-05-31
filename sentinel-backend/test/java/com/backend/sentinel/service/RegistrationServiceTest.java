package com.backend.sentinel.service;

import com.backend.sentinel.dto.RegisterRequest;
import com.backend.sentinel.entity.User;
import com.backend.sentinel.exception.UsernameAlreadyExistsException;
import com.backend.sentinel.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegistrationService Unit Tests")
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    private final RegisterRequest validRequest =
            new RegisterRequest("newuser", "newuser@example.com", "rawpassword");

    // ── happy path ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("New username: saves user to repository once")
    void saveRegisterService_newUsername_savesUser() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");

        registrationService.saveRegisterService(validRequest);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Password is encoded before saving — raw password never persisted")
    void saveRegisterService_encodesPasswordBeforeSave() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("rawpassword")).thenReturn("$2a$hashed");

        registrationService.saveRegisterService(validRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertNotEquals("rawpassword", saved.getPassword());
        assertEquals("$2a$hashed", saved.getPassword());
    }

    @Test
    @DisplayName("Saved user is assigned ROLE_USER")
    void saveRegisterService_setsRoleToRoleUser() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");

        registrationService.saveRegisterService(validRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertEquals("ROLE_USER", captor.getValue().getRole());
    }

    @Test
    @DisplayName("Saved user has the correct username from the request")
    void saveRegisterService_setsUsernameFromRequest() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$hashed");

        registrationService.saveRegisterService(validRequest);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertEquals("newuser", captor.getValue().getUsername());
    }

    // ── duplicate username ───────────────────────────────────────────────────

    @Test
    @DisplayName("Duplicate username throws UsernameAlreadyExistsException")
    void saveRegisterService_duplicateUsername_throwsException() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> registrationService.saveRegisterService(validRequest));
    }

    @Test
    @DisplayName("No save occurs when username already exists")
    void saveRegisterService_duplicateUsername_doesNotSave() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> registrationService.saveRegisterService(validRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Password is never encoded when username is a duplicate")
    void saveRegisterService_duplicateUsername_doesNotEncodePassword() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> registrationService.saveRegisterService(validRequest));

        verify(passwordEncoder, never()).encode(anyString());
    }
}
