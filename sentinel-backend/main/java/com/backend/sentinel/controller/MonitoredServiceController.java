package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.service.MonitoredServiceService;
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
@RequestMapping("/api/v1/service")
public class MonitoredServiceController {

    private final MonitoredServiceService monitoredService;

    public MonitoredServiceController(MonitoredServiceService monitoredService) {
        this.monitoredService = monitoredService;
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        ServiceResponse data = monitoredService.saveService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    /**
     * Returns a paginated list of the authenticated user's monitored services.
     * Defaults: page=0, size=20, sorted by createdAt descending.
     * Supports ?page=N&size=N&sort=name,asc via Spring's Pageable resolution.
     */
    @GetMapping
    public ResponseEntity<Page<ServiceResponse>> getAllServices(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(monitoredService.findAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        monitoredService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
