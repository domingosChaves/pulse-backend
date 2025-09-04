package com.domingos.pulse_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

@RestController
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Verificação de saúde", description = "Endpoint público de healthcheck", security = {})
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/api/health")
    @Operation(summary = "Verificação de saúde (com /api)", description = "Endpoint público de healthcheck sob /api", security = {})
    public ResponseEntity<String> apiHealth() {
        return ResponseEntity.ok("OK");
    }
}
