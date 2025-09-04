package com.domingos.pulse_backend.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDefaultUser(AuthService authService) {
        return args -> authService.ensureDefaultUser();
    }
}

