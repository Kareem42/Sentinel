package com.backend.sentinel.repository;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MonitoredServiceRepository extends JpaRepository<MonitoredServiceEntity, UUID> {
    // This will allow Spring to generate the CRUD methods(save, findById, delete) automatically.
}
