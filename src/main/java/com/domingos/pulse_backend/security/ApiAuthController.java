package com.domingos.pulse_backend.security;

import com.domingos.pulse_backend.security.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação (API)", description = "Cadastro, login, me e fluxo OAuth compatível com o frontend")
public class ApiAuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final OAuthProperties oauthProps;
    private final OAuthExchangeService oauthExchange;

    public ApiAuthController(AuthService authService, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, OAuthProperties oauthProps, OAuthExchangeService oauthExchange) {
        this.authService = authService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.oauthProps = oauthProps;
        this.oauthExchange = oauthExchange;
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastro de usuário", security = {})
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        // Normalizações
        String email = req.getEmail() != null ? req.getEmail().trim().toLowerCase() : null;
        String username = req.getUsername().trim();

        if (usuarioRepository.existsByEmail(email)) {
            return conflict("Email já utilizado");
        }
        if (usuarioRepository.existsByUsername(username)) {
            return conflict("Username já utilizado");
        }
        Usuario u = new Usuario();
        u.setNome(req.getName().trim());
        u.setEmail(email);
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setStatus(UsuarioStatus.ATIVO);
        u.setRoles("ROLE_USER");
        u.setProvedor("LOCAL");
        usuarioRepository.save(u);

        // Autenticar no ato conforme briefing
        var tokens = jwtService.tokensFor(u);
        AuthResponse resp = new AuthResponse(tokens.getAccessToken(), toUserResponse(u));
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    @Operation(summary = "Login tradicional (username/password)", security = {})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        var tokens = authService.login(req.getUsername(), req.getPassword());
        var user = usuarioRepository.findByUsername(req.getUsername()).orElseThrow();
        return ResponseEntity.ok(new AuthResponse(tokens.getAccessToken(), toUserResponse(user)));
    }

    @GetMapping("/me")
    @Operation(summary = "Usuário autenticado atual")
    public ResponseEntity<UserResponse> me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !StringUtils.hasText(auth.getName())) {
            return ResponseEntity.status(401).build();
        }
        var user = usuarioRepository.findByUsername(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(toUserResponse(user));
    }

    @GetMapping("/oauth/authorize")
    @Operation(summary = "Inicia fluxo OAuth e armazena redirect autorizado", security = {})
    public ResponseEntity<Void> oauthAuthorize(@RequestParam("provider") String provider,
                                               @RequestParam("redirect_uri") String redirect,
                                               HttpServletResponse response) {
        String p = provider.toLowerCase();
        if (!List.of("google", "github").contains(p)) {
            return ResponseEntity.badRequest().build();
        }
        // Validar redirect
        if (!isAllowedRedirect(redirect)) {
            return ResponseEntity.status(400).header("X-Error", "redirect_uri não permitido").build();
        }
        // Salvar redirect em cookie de curta duração (5 min)
        Cookie c = new Cookie("oauth_redirect", redirect);
        c.setHttpOnly(true);
        c.setPath("/");
        c.setMaxAge((int) Duration.ofMinutes(5).getSeconds());
        response.addCookie(c);
        // Redirecionar para o provedor gerenciado pelo Spring Security
        String location = "/oauth2/authorization/" + p;
        return ResponseEntity.status(302).location(URI.create(location)).build();
    }

    @PostMapping("/oauth/callback")
    @Operation(summary = "Callback manual OAuth: troca code por perfil e retorna token+user", security = {})
    public ResponseEntity<AuthResponse> oauthCallback(@Valid @RequestBody OAuthCallbackRequest body) {
        String provider = body.getProvider().toLowerCase();
        if (!List.of("google", "github").contains(provider)) {
            return ResponseEntity.badRequest().build();
        }
        // Usar o primeiro redirect permitido como redirectUri de troca
        String redirectUri = oauthProps.getAllowedRedirects().isEmpty() ? null : oauthProps.getAllowedRedirects().get(0);
        if (!StringUtils.hasText(redirectUri)) {
            throw new IllegalStateException("Nenhum redirect OAuth permitido configurado");
        }
        var profile = oauthExchange.exchange(provider, body.getCode(), redirectUri);
        // Definir username preferindo email
        String username = profile.getEmail() != null ? profile.getEmail().toLowerCase() : (provider + ":" + profile.getProviderId());
        Usuario user = usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode("oauth2-user"));
            u.setStatus(UsuarioStatus.ATIVO);
            u.setRoles("ROLE_USER");
            u.setProvedor(profile.getProvider());
            return u;
        });
        user.setEmail(profile.getEmail());
        user.setNome(profile.getName());
        user.setAvatarUrl(profile.getAvatar());
        user.setProviderId(profile.getProviderId());
        user.setProvedor(profile.getProvider());
        usuarioRepository.save(user);
        var tokens = jwtService.tokensFor(user);
        return ResponseEntity.ok(new AuthResponse(tokens.getAccessToken(), toUserResponse(user)));
    }

    private boolean isAllowedRedirect(String redirect) {
        if (!StringUtils.hasText(redirect)) return false;
        List<String> allowed = oauthProps.getAllowedRedirects();
        if (allowed == null || allowed.isEmpty()) return false;
        return allowed.stream().anyMatch(redirect::startsWith);
    }

    private UserResponse toUserResponse(Usuario u) {
        String[] roles = u.rolesAsSet().toArray(String[]::new);
        return new UserResponse(u.getId(), u.getNome(), u.getEmail(), u.getUsername(), u.getAvatarUrl(), roles);
    }

    private ResponseEntity<?> conflict(String message) {
        return ResponseEntity.status(409)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .body(new com.domingos.pulse_backend.api.ErrorResponse(message, java.time.LocalDateTime.now(), "/api/auth/register"));
    }
}
