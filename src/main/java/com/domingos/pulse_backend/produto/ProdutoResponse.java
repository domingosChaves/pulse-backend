package com.domingos.pulse_backend.produto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoResponse", description = "Representa um produto retornado pela API")
public class ProdutoResponse {
    @Schema(description = "Identificador do produto", example = "1")
    private Long id;
    @Schema(description = "Nome do produto", example = "Produto Alpha")
    private String nome;
    @Schema(description = "Código de barras", example = "7891234567895")
    private String codigoBarras;
    @Schema(description = "Descrição do produto", example = "Descrição breve do produto")
    private String descricao;
    @Schema(description = "Preço do produto", example = "19.90")
    private BigDecimal preco;
    @Schema(description = "Quantidade em estoque", example = "10")
    private Integer estoque;
    @Schema(description = "ID do fabricante do produto", example = "1")
    private Long fabricanteId;
    @Schema(description = "Nome do fabricante do produto", example = "ACME Indústria")
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
