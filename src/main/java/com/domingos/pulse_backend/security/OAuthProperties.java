package com.domingos.pulse_backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "oauth")
public class OAuthProperties {
    /** redirects permitidos, ex.: http://localhost:4200/auth/callback */
    private List<String> allowedRedirects = new ArrayList<>();

    public List<String> getAllowedRedirects() { return allowedRedirects; }
    public void setAllowedRedirects(List<String> allowedRedirects) { this.allowedRedirects = allowedRedirects; }
}

