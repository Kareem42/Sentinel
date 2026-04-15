package com.backend.sentinel.repository;

import com.backend.sentinel.entity.MonitoredService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredService, UUID> {
    // This will allow Spring to generate the CRUD methods(save, findById, delete) automatically.
}
