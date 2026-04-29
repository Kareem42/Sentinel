package com.backend.sentinel.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime,
        Map<String, String> errors // Key: Field Name, Value: error message
) {
}
