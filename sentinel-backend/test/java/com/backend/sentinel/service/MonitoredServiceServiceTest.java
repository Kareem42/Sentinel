package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoredServiceService Unit Tests")
class MonitoredServiceServiceTest {

    @Mock
    private MonitoredServiceRepository repository;

    @InjectMocks
    private MonitoredServiceService monitoredServiceService;

    // ── saveService ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("saveService maps name and url from request onto the saved entity")
    void saveService_mapsRequestFieldsCorrectly() {
        ServiceRequest request = new ServiceRequest("My API", "https://api.example.com");
        MonitoredServiceEntity saved = entityWith(UUID.randomUUID(), "My API", "https://api.example.com", "PENDING");
        when(repository.save(any())).thenReturn(saved);

        monitoredServiceService.saveService(request);

        // Capture what was actually passed to save() and verify fields
        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());
        MonitoredServiceEntity persisted = captor.getValue();

        assertEquals("My API", persisted.getName());
        assertEquals("https://api.example.com", persisted.getUrl());
    }

    @Test
    @DisplayName("saveService always sets status to PENDING — never relies on entity default")
    void saveService_setsStatusToPending() {
        when(repository.save(any())).thenReturn(entityWith(UUID.randomUUID(), "X", "https://x.com", "PENDING"));

        monitoredServiceService.saveService(new ServiceRequest("X", "https://x.com"));

        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());

        assertEquals("PENDING", captor.getValue().getStatus());
    }

    @Test
    @DisplayName("saveService returns a response built from the saved entity's fields")
    void saveService_returnsResponseFromSavedEntity() {
        UUID id = UUID.randomUUID();
        MonitoredServiceEntity saved = entityWith(id, "My API", "https://api.example.com", "PENDING");
        when(repository.save(any())).thenReturn(saved);

        ServiceResponse response = monitoredServiceService.saveService(
                new ServiceRequest("My API", "https://api.example.com"));

        assertEquals(id, response.id());
        assertEquals("My API", response.name());
        assertEquals("https://api.example.com", response.url());
        assertEquals("PENDING", response.status());
    }

    // ── findAll ──────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findAll maps all repository entities to ServiceResponse")
    void findAll_mapsEntitiesToResponses() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        when(repository.findAll()).thenReturn(List.of(
                entityWith(id1, "Service A", "https://a.com", "UP"),
                entityWith(id2, "Service B", "https://b.com", "DOWN")
        ));

        List<ServiceResponse> result = monitoredServiceService.findAll();

        assertEquals(2, result.size());
        assertEquals(id1, result.get(0).id());
        assertEquals("UP", result.get(0).status());
        assertEquals(id2, result.get(1).id());
        assertEquals("DOWN", result.get(1).status());
    }

    @Test
    @DisplayName("findAll returns empty list when no services are stored")
    void findAll_emptyRepository_returnsEmptyList() {
        when(repository.findAll()).thenReturn(List.of());

        List<ServiceResponse> result = monitoredServiceService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private MonitoredServiceEntity entityWith(UUID id, String name, String url, String status) {
        MonitoredServiceEntity e = new MonitoredServiceEntity();
        e.setId(id);
        e.setName(name);
        e.setUrl(url);
        e.setStatus(status);
        return e;
    }
}
