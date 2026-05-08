package com.backend.sentinel.config;

import com.backend.sentinel.dto.RegisterRequest;
import com.backend.sentinel.repository.UserRepository;
import com.backend.sentinel.service.RegistrationService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationService registrationService;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("chester").isEmpty()) {
            RegisterRequest adminRequest = new RegisterRequest("chester", "password123", "admin@sentinel.com");
            registrationService.saveRegisterService(adminRequest);

            System.out.println("Environment seeded via RegistrationService");
        }
    }
}