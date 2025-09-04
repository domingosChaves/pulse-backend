package com.domingos.pulse_backend.security.dto;

import jakarta.validation.constraints.NotBlank;

public class OAuthCallbackRequest {
    @NotBlank
    private String code;

    @NotBlank
    private String provider; // google|github

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
}

