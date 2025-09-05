package com.domingos.pulse_backend.fabricante;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "FabricanteResponse", description = "Representa um fabricante para o frontend")
public class FabricanteResponse {
    private Long id;
    private String nome;
    private String descricao;

    public FabricanteResponse() {}
    public FabricanteResponse(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}

