package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.service.MonitoredServiceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoredServiceController - GET All Tests")
class MonitoredServiceControllerGetAllTest {

    @Mock
    private MonitoredServiceService monitoredServiceService;

    @InjectMocks
    private MonitoredServiceController monitoredServiceController;

    private static final Pageable DEFAULT_PAGE = PageRequest.of(0, 20);

    private ServiceResponse response(String name, String url, String status) {
        return new ServiceResponse(UUID.randomUUID(), name, url, status, null, null, 60);
    }

    @Test
    @DisplayName("Returns 200 with page of services")
    void getAllServices_returnsOkWithPage() {
        Page<ServiceResponse> page = new PageImpl<>(List.of(
                response("Service A", "https://a.com", "UP"),
                response("Service B", "https://b.com", "DOWN")
        ));
        when(monitoredServiceService.findAll(any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<ServiceResponse>> result = monitoredServiceController.getAllServices(DEFAULT_PAGE);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(2, result.getBody().getContent().size());
    }

    @Test
    @DisplayName("Returns 200 with empty page — not 404 — when no services exist")
    void getAllServices_noServices_returnsEmptyPage() {
        when(monitoredServiceService.findAll(any(Pageable.class))).thenReturn(Page.empty());

        ResponseEntity<Page<ServiceResponse>> result = monitoredServiceController.getAllServices(DEFAULT_PAGE);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().getContent().isEmpty());
    }

    @Test
    @DisplayName("Delegates to service exactly once")
    void getAllServices_callsServiceOnce() {
        when(monitoredServiceService.findAll(any(Pageable.class))).thenReturn(Page.empty());

        monitoredServiceController.getAllServices(DEFAULT_PAGE);

        verify(monitoredServiceService, times(1)).findAll(any(Pageable.class));
    }
}
