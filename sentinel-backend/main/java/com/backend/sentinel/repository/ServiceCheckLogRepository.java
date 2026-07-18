package com.backend.sentinel.repository;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.entity.ServiceCheckLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceCheckLogRepository extends JpaRepository<ServiceCheckLog, UUID> {

    /** Returns the five most recent check results for a given service, newest first. */
    List<ServiceCheckLog> findTop5ByServiceOrderByCheckedAtDesc(MonitoredServiceEntity service);

    /** Deletes all check log rows for a service so it can be removed without violating the FK constraint. */
    void deleteAllByService(MonitoredServiceEntity service);
}
