package com.backend.sentinel.controller;

import com.backend.sentinel.dto.AuthResponse;
import com.backend.sentinel.dto.LoginRequest;
import com.backend.sentinel.repository.UserRepository;
import com.backend.sentinel.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        var userOpt = userRepository.findByUsername(request.username());

        if (userOpt.isPresent()) {
            String dbPassword = userOpt.get().getPassword();
            boolean matches = passwordEncoder.matches(request.password(), dbPassword);

            System.out.println("--- DEBUG AUTH ---");
            System.out.println("Username: " + request.username());
            System.out.println("Raw Input: " + request.password());
            System.out.println("DB Hash: " + dbPassword);
            System.out.println("Does BCrypt match?: " + matches);
            System.out.println("------------------");
        } else {
            System.out.println("DEBUG: User '" + request.username() + "' NOT FOUND in DB.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        String token = jwtService.generateToken(request.username());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}