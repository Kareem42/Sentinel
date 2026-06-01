package com.backend.sentinel.config;

import com.backend.sentinel.dto.RegisterRequest;
import com.backend.sentinel.entity.MonitoredServiceEntity;
import com.backend.sentinel.repository.MonitoredServiceRepository;
import com.backend.sentinel.repository.UserRepository;
import com.backend.sentinel.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MonitoredServiceRepository monitoredServiceRepository;
    private final RegistrationService registrationService;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("demo").isEmpty()) {
            registrationService.saveRegisterService(
                    new RegisterRequest("demo", "demo@sentinel.com", "demo1234")
            );
            log.info("[DataInitializer] Demo user created.");
        }

        var demoUser = userRepository.findByUsername("demo")
                .orElseThrow(() -> new IllegalStateException("Demo user not found after seeding"));

        if (monitoredServiceRepository.count() == 0) {
            monitoredServiceRepository.save(service("GitHub", "https://github.com", demoUser));
            monitoredServiceRepository.save(service("Google", "https://www.google.com", demoUser));
            monitoredServiceRepository.save(service("Example API", "https://jsonplaceholder.typicode.com/todos/1", demoUser));
            log.info("[DataInitializer] Demo monitored services seeded.");
        }
    }

    private MonitoredServiceEntity service(String name, String url, com.backend.sentinel.entity.User owner) {
        MonitoredServiceEntity s = new MonitoredServiceEntity();
        s.setName(name);
        s.setUrl(url);
        s.setStatus("UNKNOWN");
        s.setOwner(owner);
        return s;
    }
}