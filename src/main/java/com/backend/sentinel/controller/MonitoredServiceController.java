package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController("/api/v1")
public class MonitoredServiceController {

    URI uri;
    MonitoredServiceController(URI uri) {
        this.uri = uri;
    }

    @PostMapping("/service")
    public ResponseEntity<ServiceResponse> createService(@Valid @RequestBody ServiceResponse serviceResponse) {
        return ResponseEntity.created(uri).body(serviceResponse);
    }
}
