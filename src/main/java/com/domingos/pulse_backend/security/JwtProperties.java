package com.domingos.pulse_backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    /** Segredo HMAC para assinar tokens */
    private String secret;
    /** Tempo de vida do access token em segundos */
    private long accessTtl;
    /** Tempo de vida do refresh token em segundos */
    private long refreshTtl;
    /** Emissor (issuer) dos tokens */
    private String issuer = "pulse-backend";

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public long getAccessTtl() { return accessTtl; }
    public void setAccessTtl(long accessTtl) { this.accessTtl = accessTtl; }
    public long getRefreshTtl() { return refreshTtl; }
    public void setRefreshTtl(long refreshTtl) { this.refreshTtl = refreshTtl; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
}

