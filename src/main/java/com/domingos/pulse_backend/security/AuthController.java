package com.domingos.pulse_backend.security;

import com.domingos.pulse_backend.security.dto.LoginRequest;
import com.domingos.pulse_backend.security.dto.RefreshRequest;
import com.domingos.pulse_backend.security.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login, refresh e fluxo OAuth2")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza login padrão com username e password", security = {})
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse tokens = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Gera novos tokens a partir de um refresh token válido", security = {})
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        TokenResponse tokens = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/providers")
    @Operation(summary = "Lista provedores de OAuth e URLs de autorização", description = "Retorna URLs para iniciar o login social via Google e GitHub", security = {})
    public ResponseEntity<Map<String, String>> providers() {
        Map<String, String> map = new HashMap<>();
        map.put("google", "/oauth2/authorization/google");
        map.put("github", "/oauth2/authorization/github");
        return ResponseEntity.ok(map);
    }
}
