package com.domingos.pulse_backend.produto;

import java.math.BigDecimal;

public class ProdutoResponse {
    private Long id;
    private String nome;
    private String codigoBarras;
    private String descricao;
    private BigDecimal preco;
    private Integer estoque;
    private Long fabricanteId;
    private String fabricanteNome;

    public ProdutoResponse() {}

    public ProdutoResponse(Long id, String nome, String codigoBarras, String descricao, BigDecimal preco, Integer estoque, Long fabricanteId, String fabricanteNome) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.fabricanteId = fabricanteId;
        this.fabricanteNome = fabricanteNome;
    }

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public BigDecimal getPreco() { return preco; }
    public void setPreco(java.math.BigDecimal preco) { this.preco = preco; }
    public Integer getEstoque() { return estoque; }
    public void setEstoque(Integer estoque) { this.estoque = estoque; }
    public Long getFabricanteId() { return fabricanteId; }
    public void setFabricanteId(Long fabricanteId) { this.fabricanteId = fabricanteId; }
    public String getFabricanteNome() { return fabricanteNome; }
    public void setFabricanteNome(String fabricanteNome) { this.fabricanteNome = fabricanteNome; }
}

