package backend.sentinel.repository;

import backend.sentinel.entity.IntegrationEntity;
import backend.sentinel.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IntegrationRepository extends JpaRepository<IntegrationEntity, UUID> {

    /** All integrations belonging to a specific user, with pagination. */
    Page<IntegrationEntity> findByOwner(User owner, Pageable pageable);

    /** Ownership-aware lookup — used before deleting to prevent cross-user access. */
    Optional<IntegrationEntity> findByIdAndOwner(UUID id, User owner);
}
