package com.domingos.pulse_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper mapper = new ObjectMapper();

    public OAuth2SuccessHandler(UsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String registrationId = (authentication instanceof OAuth2AuthenticationToken t) ? t.getAuthorizedClientRegistrationId() : "oauth";
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attrs = oauthUser.getAttributes();

        // Extrair dados conforme provedor
        String provedor = registrationId.toUpperCase(); // GOOGLE ou GITHUB
        String providerId = null;
        String email = null;
        String nome = null;
        String avatar = null;
        String username;

        if ("GOOGLE".equals(provedor)) {
            providerId = stringAttr(attrs, "sub");
            email = stringAttr(attrs, "email");
            nome = stringAttr(attrs, "name");
            avatar = stringAttr(attrs, "picture");
        } else if ("GITHUB".equals(provedor)) {
            providerId = stringAttr(attrs, "id");
            email = stringAttr(attrs, "email"); // pode ser null dependendo das permissões
            nome = stringAttr(attrs, "name");
            if (nome == null || nome.isBlank()) nome = stringAttr(attrs, "login");
            avatar = stringAttr(attrs, "avatar_url");
        } else {
            providerId = stringAttr(attrs, "id");
            email = stringAttr(attrs, "email");
            nome = stringAttr(attrs, "name");
        }
        // Definir username preferindo email, senão providerId
        if (email != null && !email.isBlank()) {
            username = email;
        } else if (providerId != null && !providerId.isBlank()) {
            username = provedor.toLowerCase() + ":" + providerId;
        } else {
            username = authentication.getName();
        }

        // Upsert usuário
        Usuario user = usuarioRepository.findByUsername(username).orElseGet(() -> {
            Usuario u = new Usuario();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode("oauth2-user")); // placeholder
            u.setStatus(UsuarioStatus.ATIVO);
            u.setRoles("ROLE_USER");
            u.setProvedor(provedor);
            return u;
        });
        user.setEmail(email);
        user.setNome(nome);
        user.setAvatarUrl(avatar);
        user.setProviderId(providerId);
        user.setProvedor(provedor);
        usuarioRepository.save(user);

        var tokens = jwtService.tokensFor(user);

        // Se houver cookie de redirect salvo pelo /api/auth/oauth/authorize, redirecionar ao front com token no fragmento
        Optional<String> redirectOpt = getCookie(request, "oauth_redirect");
        if (redirectOpt.isPresent()) {
            String redirect = redirectOpt.get();
            String fragment = "#token=" + URLEncoder.encode(tokens.getAccessToken(), StandardCharsets.UTF_8);
            response.setStatus(302);
            response.setHeader("Location", redirect + fragment);
            return;
        }

        // Fallback: responder JSON (útil para testes manuais)
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        mapper.writeValue(response.getWriter(), tokens);
    }

    private String stringAttr(Map<String, Object> attrs, String key) {
        Object v = attrs.get(key);
        return v != null ? String.valueOf(v) : null;
    }

    private Optional<String> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return Optional.ofNullable(c.getValue());
        }
        return Optional.empty();
    }
}
