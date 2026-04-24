package com.backend.sentinel.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ServiceRequest(
        @NotBlank(message = "Service name is required")
        String name,

        @URL(message = "Must be a valid URL")
        @NotBlank(message = "URL is required")
        String url
) {}
