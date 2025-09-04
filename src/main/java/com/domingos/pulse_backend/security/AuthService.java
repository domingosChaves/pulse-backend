package com.domingos.pulse_backend.security;

import com.domingos.pulse_backend.security.dto.TokenResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public TokenResponse login(String username, String rawPassword) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, rawPassword)
        );
        // Se chegou aqui, credenciais válidas. Buscar usuário e checar status
        Usuario u = usuarioRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));
        if (u.getStatus() != UsuarioStatus.ATIVO) {
            throw new IllegalStateException("Usuário inativo/bloqueado");
        }
        return jwtService.tokensFor(u);
    }

    public TokenResponse refresh(String refreshToken) {
        try {
            Jws<Claims> jws = jwtService.parse(refreshToken);
            if (!jwtService.isRefreshToken(jws)) {
                throw new IllegalArgumentException("Token não é refresh");
            }
            String username = jws.getBody().getSubject();
            Usuario u = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário inválido"));
            if (u.getStatus() != UsuarioStatus.ATIVO) {
                throw new IllegalStateException("Usuário inativo/bloqueado");
            }
            return jwtService.tokensFor(u);
        } catch (JwtException e) {
            throw new IllegalArgumentException("Refresh token inválido/expirado");
        }
    }

    public void ensureDefaultUser() {
        // Admin padrão
        usuarioRepository.findByUsername("admin").orElseGet(() -> {
            Usuario u = new Usuario("admin", passwordEncoder.encode("admin"), UsuarioStatus.ATIVO, "ROLE_ADMIN,ROLE_USER", "LOCAL");
            return usuarioRepository.save(u);
        });
        // Usuário de teste padrão
        usuarioRepository.findByUsername("tester").orElseGet(() -> {
            Usuario u = new Usuario("tester", passwordEncoder.encode("test123"), UsuarioStatus.ATIVO, "ROLE_USER", "LOCAL");
            return usuarioRepository.save(u);
        });
    }
}
