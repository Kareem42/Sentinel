package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.service.MonitoredServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/service")
@CrossOrigin(origins = "https://localhost:5173")
public class MonitoredServiceController {
    private final MonitoredServiceService monitoredService;

    public MonitoredServiceController(MonitoredServiceService monitoredService) {
        this.monitoredService = monitoredService;
    }
    @PostMapping
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceRequest request) {
        var data = monitoredService.saveService(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }
}
