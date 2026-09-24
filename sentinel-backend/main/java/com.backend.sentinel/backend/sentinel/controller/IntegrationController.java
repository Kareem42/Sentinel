package backend.sentinel.controller;

import backend.sentinel.dto.IntegrationRequest;
import backend.sentinel.dto.IntegrationResponse;
import backend.sentinel.service.IntegrationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/integrations")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @PostMapping
    public ResponseEntity<IntegrationResponse> createIntegration(@Valid @RequestBody IntegrationRequest request) {
        IntegrationResponse data = integrationService.saveIntegration(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @GetMapping
    public ResponseEntity<Page<IntegrationResponse>> getAllIntegrations(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(integrationService.findAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntegration(@PathVariable UUID id) {
        integrationService.deleteIntegration(id);
        return ResponseEntity.noContent().build();
    }
}
