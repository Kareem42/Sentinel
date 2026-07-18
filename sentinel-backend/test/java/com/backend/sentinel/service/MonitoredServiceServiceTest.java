package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.entity.User;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import com.backend.sentinel.repository.ServiceCheckLogRepository;
import com.backend.sentinel.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MonitoredServiceService Unit Tests")
class MonitoredServiceServiceTest {

    @Mock
    private MonitoredServiceRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceCheckLogRepository checkLogRepository;

    @InjectMocks
    private MonitoredServiceService monitoredServiceService;

    private User mockUser;
    private static final Pageable DEFAULT_PAGE = PageRequest.of(0, 20);

    @BeforeEach
    void setUp() {
        // Wire a fake authenticated user into the SecurityContext so
        // MonitoredServiceService.currentUser() can resolve successfully.
        mockUser = new User();
        mockUser.setUsername("testuser");

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("testuser");

        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // ── saveService ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("saveService maps name and url from request onto the saved entity")
    void saveService_mapsRequestFieldsCorrectly() {
        ServiceRequest request = new ServiceRequest("My API", "https://api.example.com", null);
        MonitoredServiceEntity saved = entityWith(UUID.randomUUID(), "My API", "https://api.example.com", "PENDING");
        when(repository.save(any())).thenReturn(saved);

        monitoredServiceService.saveService(request);

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

        monitoredServiceService.saveService(new ServiceRequest("X", "https://x.com", null));

        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());

        assertEquals("PENDING", captor.getValue().getStatus());
    }

    @Test
    @DisplayName("saveService sets the owner to the authenticated user")
    void saveService_setsOwner() {
        when(repository.save(any())).thenReturn(entityWith(UUID.randomUUID(), "X", "https://x.com", "PENDING"));

        monitoredServiceService.saveService(new ServiceRequest("X", "https://x.com", null));

        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());

        assertEquals(mockUser, captor.getValue().getOwner());
    }

    @Test
    @DisplayName("saveService uses default interval of 60 when none specified")
    void saveService_defaultsIntervalTo60() {
        when(repository.save(any())).thenReturn(entityWith(UUID.randomUUID(), "X", "https://x.com", "PENDING"));

        monitoredServiceService.saveService(new ServiceRequest("X", "https://x.com", null));

        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());

        assertEquals(60, captor.getValue().getCheckIntervalSeconds());
    }

    @Test
    @DisplayName("saveService applies a custom interval when provided")
    void saveService_appliesCustomInterval() {
        when(repository.save(any())).thenReturn(entityWith(UUID.randomUUID(), "X", "https://x.com", "PENDING"));

        monitoredServiceService.saveService(new ServiceRequest("X", "https://x.com", 300));

        ArgumentCaptor<MonitoredServiceEntity> captor = ArgumentCaptor.forClass(MonitoredServiceEntity.class);
        verify(repository).save(captor.capture());

        assertEquals(300, captor.getValue().getCheckIntervalSeconds());
    }

    @Test
    @DisplayName("saveService returns a response built from the saved entity's fields")
    void saveService_returnsResponseFromSavedEntity() {
        UUID id = UUID.randomUUID();
        MonitoredServiceEntity saved = entityWith(id, "My API", "https://api.example.com", "PENDING");
        when(repository.save(any())).thenReturn(saved);

        ServiceResponse response = monitoredServiceService.saveService(
                new ServiceRequest("My API", "https://api.example.com", null));

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
        Page<MonitoredServiceEntity> entityPage = new PageImpl<>(List.of(
                entityWith(id1, "Service A", "https://a.com", "UP"),
                entityWith(id2, "Service B", "https://b.com", "DOWN")
        ));
        when(repository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(entityPage);

        Page<ServiceResponse> result = monitoredServiceService.findAll(DEFAULT_PAGE);

        assertEquals(2, result.getContent().size());
        assertEquals(id1, result.getContent().get(0).id());
        assertEquals("UP", result.getContent().get(0).status());
        assertEquals(id2, result.getContent().get(1).id());
        assertEquals("DOWN", result.getContent().get(1).status());
    }

    @Test
    @DisplayName("findAll returns empty page when no services are stored")
    void findAll_emptyRepository_returnsEmptyPage() {
        when(repository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(Page.empty());

        Page<ServiceResponse> result = monitoredServiceService.findAll(DEFAULT_PAGE);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    @DisplayName("findAll scopes query to the authenticated user")
    void findAll_scopesToCurrentUser() {
        when(repository.findByOwner(any(User.class), any(Pageable.class))).thenReturn(Page.empty());

        monitoredServiceService.findAll(DEFAULT_PAGE);

        verify(repository).findByOwner(eq(mockUser), any(Pageable.class));
    }

    // ── deleteService ────────────────────────────────────────────────────────

    @Test
    @DisplayName("deleteService removes check logs before deleting the service to satisfy the FK constraint")
    void deleteService_removesCheckLogsBeforeEntity() {
        UUID id = UUID.randomUUID();
        MonitoredServiceEntity entity = entityWith(id, "X", "https://x.com", "UP");
        when(repository.findByIdAndOwner(id, mockUser)).thenReturn(Optional.of(entity));

        monitoredServiceService.deleteService(id);

        InOrder inOrder = inOrder(checkLogRepository, repository);
        inOrder.verify(checkLogRepository).deleteAllByService(entity);
        inOrder.verify(repository).delete(entity);
    }

    @Test
    @DisplayName("deleteService throws 404 when the service does not exist or belongs to another user")
    void deleteService_notFound_throws404() {
        UUID id = UUID.randomUUID();
        when(repository.findByIdAndOwner(id, mockUser)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> monitoredServiceService.deleteService(id));

        assertEquals(404, ex.getStatusCode().value());
        verifyNoInteractions(checkLogRepository);
        verify(repository, never()).delete(any());
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private MonitoredServiceEntity entityWith(UUID id, String name, String url, String status) {
        MonitoredServiceEntity e = new MonitoredServiceEntity();
        e.setId(id);
        e.setName(name);
        e.setUrl(url);
        e.setStatus(status);
        e.setCheckIntervalSeconds(60);
        return e;
    }
}
