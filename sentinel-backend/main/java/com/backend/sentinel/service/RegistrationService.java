package com.backend.sentinel.service;

import com.backend.sentinel.dto.RegisterRequest;
import com.backend.sentinel.entity.User;
import com.backend.sentinel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveRegisterService(RegisterRequest request) throws RuntimeException {
        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username is already in use");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");

        userRepository.save(user);
    }
}