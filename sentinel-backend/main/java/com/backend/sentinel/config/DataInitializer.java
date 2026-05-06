package com.backend.sentinel.config;

import com.backend.sentinel.entity.User;
import com.backend.sentinel.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("chester").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("chester");
            testUser.setPassword(passwordEncoder.encode("password123"));
            testUser.setRole("ROLE_USER");

            userRepository.save(testUser);
            System.out.println("User created");
        } else  {
            System.out.println("User already exists");
        }
    }
}

