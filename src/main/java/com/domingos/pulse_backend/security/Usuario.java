package com.domingos.pulse_backend.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuarios_username", columnNames = {"username"}),
        @UniqueConstraint(name = "uk_usuarios_email", columnNames = {"email"}),
        @UniqueConstraint(name = "uk_usuarios_provider", columnNames = {"provedor", "provider_id"})
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false, length = 20)
    private UsuarioStatus status = UsuarioStatus.ATIVO;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String roles = "ROLE_USER";

    @NotBlank
    @Column(nullable = false, length = 20)
    private String provedor = "LOCAL"; // LOCAL, GOOGLE, GITHUB

    // Campos opcionais para OAuth e cadastro
    @Column(name = "email", length = 180)
    private String email;

    @Column(name = "nome", length = 120)
    private String nome;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "provider_id", length = 120)
    private String providerId; // sub (google) ou id (github)

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Usuario() {}

    public Usuario(String username, String password, UsuarioStatus status, String roles, String provedor) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.provedor = provedor;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UsuarioStatus getStatus() { return status; }
    public void setStatus(UsuarioStatus status) { this.status = status; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getProvedor() { return provedor; }
    public void setProvedor(String provedor) { this.provedor = provedor; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public Set<String> rolesAsSet() {
        return Stream.of(roles.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}
