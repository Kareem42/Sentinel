package com.backend.sentinel.service;

import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class MonitoringService {

    private final MonitoredServiceRepository repository;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public MonitoringService(MonitoredServiceRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkAllServices() {
        log.info("Starting health check for all services...");
        List<MonitoredServiceEntity> services = repository.findAll();

        for (MonitoredServiceEntity service : services) {
            checkAllStatus(service);
        }
    }

    private void checkAllStatus(MonitoredServiceEntity service) {
        try{
            String finalUrl = normalizeUrl(service.getUrl());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(finalUrl))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

            if(response.statusCode() >= 200 && response.statusCode() < 400) {
                service.setStatus("UP");
            } else {
                service.setStatus("DOWN");
            }
        } catch(Exception e) {
            log.error("Failed to reach service {}: {}", service.getUrl(), e.getMessage());
            service.setStatus("DOWN");
        }

        service.setLastChecked(LocalDateTime.now());
        repository.save(service);
    }

    private String normalizeUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            return "http://" + url;
        }
        return url;
    }
}
