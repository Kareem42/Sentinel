package com.backend.sentinel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monitored_services")
public class MonitoredServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    private String status = "UNKNOWN"; // "UP", "DOWN", "PENDING"
    private Instant lastChecked;

    /** Milliseconds taken on the most recent health check. Null until first check. */
    private Long lastResponseTimeMs;

    /** How often (in seconds) this service should be pinged. Defaults to 60s. */
    @Column(nullable = false)
    private int checkIntervalSeconds = 60;

    /** The user who registered this service. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
