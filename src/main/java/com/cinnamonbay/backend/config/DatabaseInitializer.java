package com.cinnamonbay.backend.config;

import com.cinnamonbay.backend.model.Role;
import com.cinnamonbay.backend.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        try {
            createRoleIfNotExists("ROLE_USER");
            createRoleIfNotExists("ROLE_ADMIN");
            log.info("Database initialization completed successfully");
        } catch (Exception e) {
            log.error("Error during database initialization: {}", e.getMessage());
        }
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleRepository.save(role);
            log.info("Created default role: {}", roleName);
        } else {
            log.info("Role already exists: {}", roleName);
        }
    }
}