package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import com.backend.sentinel.entity.MonitorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MonitoredServiceService {

    @Transactional(readOnly = true)
    public ServiceResponse saveService(@Valid ServiceRequest request) {
       // 1. Create a new Entity object and set its fields from the request
        MonitorService service = new MonitorService();

       // 2. Call repository.save(entity)

       // Convert that saved entity into a ServiceResponse and return it

       return null;
   }

}
