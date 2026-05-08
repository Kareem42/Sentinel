package com.backend.sentinel.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @NotBlank(message = "Valid username is required")
        String username,

        @NotBlank(message = "Valid email is required")
        String email,

        @NotBlank(message = "Password must be at least 8 characters or more")
        String password
) {}
