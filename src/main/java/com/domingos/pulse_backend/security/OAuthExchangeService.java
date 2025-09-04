package com.domingos.pulse_backend.security;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class OAuthExchangeService {

    private final OAuthClientProperties clients;

    public OAuthExchangeService(OAuthClientProperties clients) {
        this.clients = clients;
    }

    public OAuthProfile exchange(String provider, String code, String redirectUri) {
        String p = provider.toLowerCase();
        return switch (p) {
            case "google" -> exchangeGoogle(code, redirectUri);
            case "github" -> exchangeGithub(code, redirectUri);
            default -> throw new IllegalArgumentException("Provedor inválido");
        };
    }

    private OAuthProfile exchangeGoogle(String code, String redirectUri) {
        var conf = clients.getGoogle();
        require(conf.getClientId(), "GOOGLE_CLIENT_ID");
        require(conf.getClientSecret(), "GOOGLE_CLIENT_SECRET");

        RestTemplate rt = new RestTemplate();
        String tokenUrl = "https://oauth2.googleapis.com/token";
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", conf.getClientId());
        form.add("client_secret", conf.getClientSecret());
        form.add("code", code);
        form.add("grant_type", "authorization_code");
        form.add("redirect_uri", redirectUri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        var tokenRes = rt.exchange(tokenUrl, HttpMethod.POST, new HttpEntity<>(form, headers), Map.class);
        Map<?,?> body = tokenRes.getBody();
        if (body == null || body.get("access_token") == null) throw new IllegalArgumentException("Code inválido");
        String accessToken = String.valueOf(body.get("access_token"));

        // Buscar perfil
        HttpHeaders h2 = new HttpHeaders();
        h2.setBearerAuth(accessToken);
        var userRes = rt.exchange("https://www.googleapis.com/oauth2/v3/userinfo", HttpMethod.GET, new HttpEntity<>(h2), Map.class);
        Map<?,?> u = userRes.getBody();
        OAuthProfile p = new OAuthProfile();
        p.setProvider("GOOGLE");
        p.setProviderId(asString(u, "sub"));
        p.setEmail(asString(u, "email"));
        p.setName(asString(u, "name"));
        p.setAvatar(asString(u, "picture"));
        return p;
    }

    private OAuthProfile exchangeGithub(String code, String redirectUri) {
        var conf = clients.getGithub();
        require(conf.getClientId(), "GITHUB_CLIENT_ID");
        require(conf.getClientSecret(), "GITHUB_CLIENT_SECRET");

        RestTemplate rt = new RestTemplate();
        String tokenUrl = "https://github.com/login/oauth/access_token";
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", conf.getClientId());
        form.add("client_secret", conf.getClientSecret());
        form.add("code", code);
        form.add("redirect_uri", redirectUri);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        var tokenRes = rt.exchange(tokenUrl, HttpMethod.POST, new HttpEntity<>(form, headers), Map.class);
        Map<?,?> body = tokenRes.getBody();
        if (body == null || body.get("access_token") == null) throw new IllegalArgumentException("Code inválido");
        String accessToken = String.valueOf(body.get("access_token"));

        // Buscar perfil
        HttpHeaders h2 = new HttpHeaders();
        h2.setBearerAuth(accessToken);
        h2.setAccept(java.util.List.of(MediaType.APPLICATION_JSON));
        var userRes = rt.exchange("https://api.github.com/user", HttpMethod.GET, new HttpEntity<>(h2), Map.class);
        Map<?,?> u = userRes.getBody();
        OAuthProfile p = new OAuthProfile();
        p.setProvider("GITHUB");
        p.setProviderId(asString(u, "id"));
        String name = asString(u, "name");
        if (name == null || name.isBlank()) name = asString(u, "login");
        p.setName(name);
        p.setAvatar(asString(u, "avatar_url"));
        // Email pode não vir direto; tenta endpoint de emails (melhor esforço)
        try {
            var emailsRes = rt.exchange("https://api.github.com/user/emails", HttpMethod.GET, new HttpEntity<>(h2), java.util.List.class);
            if (emailsRes.getBody() instanceof java.util.List<?> list && !list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map<?,?> m) {
                    Object email = m.get("email");
                    if (email != null) p.setEmail(String.valueOf(email));
                }
            }
        } catch (Exception ignored) {}
        return p;
    }

    private void require(String v, String name) {
        if (v == null || v.isBlank()) throw new IllegalStateException("Credenciais OAuth ausentes: " + name);
    }

    private String asString(Map<?,?> map, String key) {
        if (map == null) return null;
        Object v = map.get(key);
        return v != null ? String.valueOf(v) : null;
    }

    public static class OAuthProfile {
        private String provider; // GOOGLE|GITHUB
        private String providerId;
        private String email;
        private String name;
        private String avatar;
        public String getProvider() { return provider; }
        public void setProvider(String provider) { this.provider = provider; }
        public String getProviderId() { return providerId; }
        public void setProviderId(String providerId) { this.providerId = providerId; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getAvatar() { return avatar; }
        public void setAvatar(String avatar) { this.avatar = avatar; }
    }
}

