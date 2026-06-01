package com.backend.sentinel.controller;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.service.MonitoredServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoredServiceController Unit Tests")
class ServiceControllerTest {

    @Mock
    private MonitoredServiceService monitoredServiceService;

    @InjectMocks
    private MonitoredServiceController monitoredServiceController;

    private ServiceRequest validRequest;
    private ServiceResponse mockResponse;
    private UUID testUUID;

    @BeforeEach
    void setUp() {
        testUUID = UUID.randomUUID();
        // checkIntervalSeconds is optional (null → defaults to 60 in the service)
        validRequest = new ServiceRequest("Test Service", "https://example.com", null);
        mockResponse = new ServiceResponse(testUUID, "Test Service", "https://example.com", "PENDING", null, null, 60);
    }

    @Test
    @DisplayName("Should create service successfully with valid request")
    void testCreateServiceSuccess() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(validRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUUID, response.getBody().id());
        assertEquals("Test Service", response.getBody().name());
        assertEquals("https://example.com", response.getBody().url());
        assertEquals("PENDING", response.getBody().status());

        verify(monitoredServiceService, times(1)).saveService(any(ServiceRequest.class));
    }

    @Test
    @DisplayName("Should call service method once when creating service")
    void testServiceMethodCalledOnce() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        monitoredServiceController.createService(validRequest);

        verify(monitoredServiceService, times(1)).saveService(any(ServiceRequest.class));
    }

    @Test
    @DisplayName("Should return CREATED status")
    void testCreateServiceReturnsCreatedStatus() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(validRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Should return correct response body")
    void testCreateServiceReturnsCorrectResponse() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(validRequest);

        assertNotNull(response.getBody());
        assertEquals(testUUID, response.getBody().id());
        assertEquals("Test Service", response.getBody().name());
        assertEquals("https://example.com", response.getBody().url());
        assertEquals("PENDING", response.getBody().status());
    }

    @Test
    @DisplayName("Should handle different service names")
    void testCreateServiceWithDifferentName() {
        String differentName = "Different Service";
        ServiceRequest differentRequest = new ServiceRequest(differentName, "https://different.com", null);
        ServiceResponse differentResponse = new ServiceResponse(testUUID, differentName, "https://different.com", "PENDING", null, null, 60);

        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(differentResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(differentRequest);

        assertEquals(differentName, response.getBody().name());
    }

    @Test
    @DisplayName("Should handle different service URLs")
    void testCreateServiceWithDifferentUrl() {
        String differentUrl = "https://newservice.io";
        ServiceRequest differentRequest = new ServiceRequest("Service Name", differentUrl, null);
        ServiceResponse differentResponse = new ServiceResponse(testUUID, "Service Name", differentUrl, "PENDING", null, null, 60);

        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(differentResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(differentRequest);

        assertEquals(differentUrl, response.getBody().url());
    }

    @Test
    @DisplayName("Should return service response with all fields")
    void testCreateServiceResponseStructure() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        ResponseEntity<ServiceResponse> response = monitoredServiceController.createService(validRequest);

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().id());
        assertNotNull(response.getBody().name());
        assertNotNull(response.getBody().url());
        assertNotNull(response.getBody().status());
    }

    @Test
    @DisplayName("Should pass request to service with correct parameters")
    void testRequestPassedCorrectlyToService() {
        when(monitoredServiceService.saveService(any(ServiceRequest.class))).thenReturn(mockResponse);

        monitoredServiceController.createService(validRequest);

        verify(monitoredServiceService).saveService(validRequest);
    }
}
