package backend.sentinel.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record IntegrationResponse(
        UUID id,
        String name,
        String purposeDescription,
        String appUrl,
        String apiDescription,
        String apiWebsiteUrl,
        LocalDateTime createdAt
) {}
