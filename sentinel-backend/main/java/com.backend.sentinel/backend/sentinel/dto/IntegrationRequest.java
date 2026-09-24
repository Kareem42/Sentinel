package backend.sentinel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record IntegrationRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Purpose description is required")
        @Size(max = 1000, message = "Purpose description cannot exceed 1000 characters")
        String purposeDescription,

        @URL(message = "Must be a valid URL")
        @NotBlank(message = "App URL is required")
        String appUrl,

        @NotBlank(message = "API description is required")
        @Size(max = 1000, message = "API description cannot exceed 1000 characters")
        String apiDescription,

        @URL(message = "Must be a valid URL")
        @NotBlank(message = "API website URL is required")
        String apiWebsiteUrl
) {}
