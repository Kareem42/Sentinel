package com.backend.sentinel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "monitored_services")
public class MonitoredServiceEntity {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    private String status = "UNKNOWN"; // "UP" , "DOWN"
    private LocalDateTime lastChecked;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
