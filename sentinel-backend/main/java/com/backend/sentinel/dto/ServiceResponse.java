package com.backend.sentinel.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String url,
        String status,
        LocalDateTime lastChecked,
        Long lastResponseTimeMs,
        int checkIntervalSeconds
) {}

