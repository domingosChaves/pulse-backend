package com.domingos.pulse_backend.security;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuarios_username", columnNames = {"username"})
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private UsuarioStatus status = UsuarioStatus.ATIVO;

    /** Roles separadas por vírgula (ex.: ROLE_ADMIN,ROLE_USER) */
    @NotBlank
    private String roles = "ROLE_USER";

    /** Provedor de login: LOCAL ou OAUTH */
    @NotBlank
    private String provedor = "LOCAL";

    public Usuario() {}

    public Usuario(String username, String password, UsuarioStatus status, String roles, String provedor) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.provedor = provedor;
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

    public Set<String> rolesAsSet() {
        return Stream.of(roles.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }
}

