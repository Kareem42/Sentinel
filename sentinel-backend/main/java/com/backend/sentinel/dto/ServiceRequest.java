package com.backend.sentinel.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public record ServiceRequest(
        @NotBlank(message = "Service name is required")
        String name,

        @URL(message = "Must be a valid URL")
        @NotBlank(message = "URL is required")
        String url,

        /**
         * How often (in seconds) this service should be pinged.
         * Must be between 30 s and 86400 s (24 h). Defaults to 60 s if omitted.
         */
        @Min(value = 30, message = "Check interval must be at least 30 seconds")
        @Max(value = 86400, message = "Check interval cannot exceed 86400 seconds (24 hours)")
        Integer checkIntervalSeconds
) {
    /** Canonical default when the caller omits checkIntervalSeconds. */
    public int resolvedInterval() {
        return checkIntervalSeconds != null ? checkIntervalSeconds : 60;
    }
}
