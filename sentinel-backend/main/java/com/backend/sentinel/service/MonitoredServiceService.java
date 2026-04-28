package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MonitoredServiceService {
    private final MonitoredServiceRepository repository;

    @Transactional
    public ServiceResponse saveService(ServiceRequest request) {
        MonitoredServiceEntity serviceEntity = new MonitoredServiceEntity();
        serviceEntity.setName(request.name());
        serviceEntity.setUrl(request.url());
        serviceEntity.setStatus("PENDING");

        MonitoredServiceEntity savedEntity = repository.save(serviceEntity);
       return new ServiceResponse(
               savedEntity.getId(),
               savedEntity.getName(),
               savedEntity.getUrl(),
               savedEntity.getStatus());
   }

   public List<ServiceResponse> findAll(){
        return repository.findAll().stream()
                .map(monitoredServiceEntity -> new ServiceResponse(
                        monitoredServiceEntity.getId(),
                        monitoredServiceEntity.getName(),
                        monitoredServiceEntity.getUrl(),
                        monitoredServiceEntity.getStatus()
                ))
                .toList();
   }
}
