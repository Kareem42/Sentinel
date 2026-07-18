package com.backend.sentinel.service;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Scheduler that fires a base tick every 30 seconds and dispatches
 * parallel health checks for any service whose check interval has elapsed.
 * HTTP work is delegated to MonitoringCheckExecutor so each service check
 * runs in its own transaction on the monitoringExecutor thread pool —
 * preventing one slow/hung endpoint from blocking all others.
 */
@Slf4j
@Service
public class MonitoringService {

    private final MonitoredServiceRepository repository;
    private final MonitoringCheckExecutor checkExecutor;
    private final Executor monitoringExecutor;

    public MonitoringService(
            MonitoredServiceRepository repository,
            MonitoringCheckExecutor checkExecutor,
            @Qualifier("monitoringExecutor") Executor monitoringExecutor) {
        this.repository = repository;
        this.checkExecutor = checkExecutor;
        this.monitoringExecutor = monitoringExecutor;
    }

    /**
     * Ticks every 30 s (the minimum supported check interval).
     * For each service, checks whether its individual checkIntervalSeconds
     * has elapsed before dispatching a ping.
     */
    @Scheduled(fixedRate = 30_000)
    public void checkAllServices() {
        List<MonitoredServiceEntity> services = repository.findAll();

        List<UUID> due = services.stream()
                .filter(this::isDue)
                .map(MonitoredServiceEntity::getId)
                .toList();

        if (due.isEmpty()) {
            log.debug("Monitoring tick: no services due for a check.");
            return;
        }

        log.info("Monitoring tick: dispatching checks for {} / {} service(s).", due.size(), services.size());

        List<CompletableFuture<Void>> futures = due.stream()
                .map(id -> CompletableFuture.runAsync(() -> checkExecutor.performCheck(id), monitoringExecutor)
                        .exceptionally(ex -> {
                            log.error("Unexpected error during check for service {}: {}", id, ex.getMessage());
                            return null;
                        }))
                .toList();

        // Wait for all checks in this tick to complete before the next tick fires
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private boolean isDue(MonitoredServiceEntity service) {
        if (service.getLastChecked() == null) return true;
        Instant nextDue = service.getLastChecked().plusSeconds(service.getCheckIntervalSeconds());
        return !Instant.now().isBefore(nextDue);
    }
}
