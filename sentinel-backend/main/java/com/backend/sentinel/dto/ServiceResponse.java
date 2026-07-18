package com.backend.sentinel.dto;

import java.time.Instant;
import java.util.UUID;

public record ServiceResponse(
        UUID id,
        String name,
        String url,
        String status,
        Instant lastChecked,
        Long lastResponseTimeMs,
        int checkIntervalSeconds
) {}

