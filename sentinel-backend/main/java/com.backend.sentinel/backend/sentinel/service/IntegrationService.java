package backend.sentinel.service;

import backend.sentinel.dto.IntegrationRequest;
import backend.sentinel.dto.IntegrationResponse;
import backend.sentinel.entity.IntegrationEntity;
import backend.sentinel.entity.User;
import backend.sentinel.repository.IntegrationRepository;
import backend.sentinel.repository.UserRepository;
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
public class IntegrationService {

    private final IntegrationRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public IntegrationResponse saveIntegration(IntegrationRequest request) {
        User owner = currentUser();

        IntegrationEntity entity = new IntegrationEntity();
        entity.setName(request.name());
        entity.setPurposeDescription(request.purposeDescription());
        entity.setAppUrl(request.appUrl());
        entity.setApiDescription(request.apiDescription());
        entity.setApiWebsiteUrl(request.apiWebsiteUrl());
        entity.setOwner(owner);

        IntegrationEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<IntegrationResponse> findAll(Pageable pageable) {
        User owner = currentUser();
        return repository.findByOwner(owner, pageable).map(this::toResponse);
    }

    @Transactional
    public void deleteIntegration(UUID id) {
        User owner = currentUser();
        IntegrationEntity entity = repository.findByIdAndOwner(id, owner)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Integration not found or does not belong to you"));
        repository.delete(entity);
    }

    // -------------------------------------------------------------------------

    private User currentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    private IntegrationResponse toResponse(IntegrationEntity e) {
        return new IntegrationResponse(
                e.getId(),
                e.getName(),
                e.getPurposeDescription(),
                e.getAppUrl(),
                e.getApiDescription(),
                e.getApiWebsiteUrl(),
                e.getCreatedAt()
        );
    }
}
