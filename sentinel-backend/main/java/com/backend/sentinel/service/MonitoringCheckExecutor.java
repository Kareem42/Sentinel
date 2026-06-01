package com.backend.sentinel.service;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.entity.ServiceCheckLog;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import com.backend.sentinel.repository.ServiceCheckLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Performs a single health-check ping for one service and persists the result.
 * Kept as a separate Spring bean so each check runs in its own transaction,
 * which allows MonitoringService to dispatch checks in parallel without
 * holding a single long-lived transaction across all services.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoringCheckExecutor {

    private final MonitoredServiceRepository serviceRepository;
    private final ServiceCheckLogRepository checkLogRepository;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    @Transactional
    public void performCheck(UUID serviceId) {
        MonitoredServiceEntity service = serviceRepository.findById(serviceId).orElse(null);
        if (service == null) return;

        String status;
        Long responseTimeMs = null;

        try {
            String url = normalizeUrl(service.getUrl());
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .timeout(Duration.ofSeconds(10))
                    .build();

            long start = System.currentTimeMillis();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            responseTimeMs = System.currentTimeMillis() - start;

            status = (response.statusCode() >= 200 && response.statusCode() < 400) ? "UP" : "DOWN";

        } catch (Exception e) {
            log.warn("Health check failed for '{}' ({}): {}", service.getName(), service.getUrl(), e.getMessage());
            status = "DOWN";
        }

        // Update denormalized fields on the service for fast list queries
        service.setStatus(status);
        service.setLastChecked(LocalDateTime.now());
        service.setLastResponseTimeMs(responseTimeMs);
        serviceRepository.save(service);

        // Append to check history
        ServiceCheckLog log = new ServiceCheckLog();
        log.setService(service);
        log.setStatus(status);
        log.setResponseTimeMs(responseTimeMs);
        checkLogRepository.save(log);
    }

    private String normalizeUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "https://" + url;
        }
        return url;
    }
}
