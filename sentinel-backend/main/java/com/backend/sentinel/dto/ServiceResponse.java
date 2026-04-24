package com.backend.sentinel.dto;

import java.util.UUID;

public record ServiceResponse(UUID id, String name, String url, String status) {
}
