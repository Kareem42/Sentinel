package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitoredService;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MonitoredServiceService {
    private final MonitoredServiceRepository monitoredServiceRepository;

    @Transactional
    public ServiceResponse saveService(ServiceRequest request) {
       // Map Request DTO -> Entity
        MonitoredService serviceEntity = new MonitoredService();
       // Setting the name and url from the request
        serviceEntity.setName(request.name());
        serviceEntity.setUrl(request.url());
        serviceEntity.setStatus("PENDING");

        MonitoredService savedEntity = monitoredServiceRepository.save(serviceEntity);
       return new ServiceResponse(
               savedEntity.getId(),
               savedEntity.getName(),
               savedEntity.getUrl(),
               savedEntity.getStatus());
   }
}
