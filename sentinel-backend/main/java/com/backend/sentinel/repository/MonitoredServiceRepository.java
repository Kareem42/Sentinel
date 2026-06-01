package com.backend.sentinel.repository;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredServiceEntity, UUID> {

    /** All services belonging to a specific user, with pagination. */
    Page<MonitoredServiceEntity> findByOwner(User owner, Pageable pageable);

    /** Ownership-aware lookup — used before deleting to prevent cross-user access. */
    Optional<MonitoredServiceEntity> findByIdAndOwner(UUID id, User owner);
}
