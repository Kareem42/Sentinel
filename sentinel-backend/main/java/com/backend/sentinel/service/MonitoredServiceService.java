package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.entity.User;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import com.backend.sentinel.repository.ServiceCheckLogRepository;
import com.backend.sentinel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MonitoredServiceService {

    private final MonitoredServiceRepository repository;
    private final UserRepository userRepository;
    private final ServiceCheckLogRepository checkLogRepository;

    @Transactional
    public ServiceResponse saveService(ServiceRequest request) {
        User owner = currentUser();

        MonitoredServiceEntity entity = new MonitoredServiceEntity();
        entity.setName(request.name());
        entity.setUrl(request.url());
        entity.setStatus("PENDING");
        entity.setCheckIntervalSeconds(request.resolvedInterval());
        entity.setOwner(owner);

        MonitoredServiceEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ServiceResponse> findAll(Pageable pageable) {
        User owner = currentUser();
        return repository.findByOwner(owner, pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteService(UUID id) {
        User owner = currentUser();
        MonitoredServiceEntity entity = repository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Service not found or does not belong to you"));
        checkLogRepository.deleteAllByService(entity);
        repository.delete(entity);
    }

    // -------------------------------------------------------------------------

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private ServiceResponse toResponse(MonitoredServiceEntity e) {
        return new ServiceResponse(
                e.getId(),
                e.getName(),
                e.getUrl(),
                e.getStatus(),
                e.getLastChecked(),
                e.getLastResponseTimeMs(),
                e.getCheckIntervalSeconds()
        );
    }
}
