package com.domingos.pulse_backend.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "oauth.clients")
public class OAuthClientProperties {

    private Provider google = new Provider();
    private Provider github = new Provider();

    public Provider getGoogle() { return google; }
    public void setGoogle(Provider google) { this.google = google; }
    public Provider getGithub() { return github; }
    public void setGithub(Provider github) { this.github = github; }

    public static class Provider {
        private String clientId;
        private String clientSecret;

        public String getClientId() { return clientId; }
        public void setClientId(String clientId) { this.clientId = clientId; }
        public String getClientSecret() { return clientSecret; }
        public void setClientSecret(String clientSecret) { this.clientSecret = clientSecret; }
    }
}

