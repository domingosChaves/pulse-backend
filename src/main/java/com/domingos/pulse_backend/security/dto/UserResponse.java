package com.domingos.pulse_backend.security.dto;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String username;
    private String avatarUrl;
    private String[] roles;

    public UserResponse() {}

    public UserResponse(Long id, String name, String email, String username, String avatarUrl, String[] roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String[] getRoles() { return roles; }
    public void setRoles(String[] roles) { this.roles = roles; }
}

