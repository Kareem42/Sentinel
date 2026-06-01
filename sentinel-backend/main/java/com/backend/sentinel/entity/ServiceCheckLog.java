package com.backend.sentinel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable record of a single health-check ping for a monitored service.
 * Rows are append-only — never updated after creation.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_check_logs", indexes = {
        @Index(name = "idx_check_log_service_checked", columnList = "service_id, checked_at DESC")
})
public class ServiceCheckLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_id", nullable = false)
    private MonitoredServiceEntity service;

    /** Result of this specific ping: "UP" or "DOWN". */
    @Column(nullable = false)
    private String status;

    /** Round-trip time in milliseconds. Null if the request never completed. */
    private Long responseTimeMs;

    @CreationTimestamp
    @Column(name = "checked_at", nullable = false, updatable = false)
    private LocalDateTime checkedAt;
}
