package com.backend.sentinel.service;

import com.backend.sentinel.dto.ServiceRequest;
import com.backend.sentinel.dto.ServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MonitoredServiceService {
    private String id;
    private ServiceRequest serviceRequest;
    private ServiceResponse serviceResponse;

   public ServiceResponse saveService(@Valid ServiceRequest request) {
       // 1. Create a new Entity object and set its fields from the request

       // 2. Call repository.save(entity)

       // Convert that saved entity into a ServiceResponse and return it

       return null;
   }

}
