package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.service.MonitoredServiceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoredServiceController - GET All Tests")
class MonitoredServiceControllerGetAllTest {

    @Mock
    private MonitoredServiceService monitoredServiceService;

    @InjectMocks
    private MonitoredServiceController monitoredServiceController;

    @Test
    @DisplayName("Returns 200 with list of services")
    void getAllService_returnsOkWithList() {
        List<ServiceResponse> services = List.of(
                new ServiceResponse(UUID.randomUUID(), "Service A", "https://a.com", "UP"),
                new ServiceResponse(UUID.randomUUID(), "Service B", "https://b.com", "DOWN")
        );
        when(monitoredServiceService.findAll()).thenReturn(services);

        ResponseEntity<List<ServiceResponse>> response = monitoredServiceController.getAllService();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Returns 200 with empty list — not 404 — when no services exist")
    void getAllService_noServices_returnsEmptyList() {
        when(monitoredServiceService.findAll()).thenReturn(List.of());

        ResponseEntity<List<ServiceResponse>> response = monitoredServiceController.getAllService();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    @DisplayName("Delegates to service exactly once")
    void getAllService_callsServiceOnce() {
        when(monitoredServiceService.findAll()).thenReturn(List.of());

        monitoredServiceController.getAllService();

        verify(monitoredServiceService, times(1)).findAll();
    }
}
