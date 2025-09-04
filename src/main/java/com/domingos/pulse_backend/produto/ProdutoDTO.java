package com.domingos.pulse_backend.produto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProdutoDTO", description = "Payload para criação/atualização de produto")
public class ProdutoDTO {

    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(max = 255)
    @Schema(description = "Nome do produto", example = "Produto Alpha")
    private String nome;

    @NotBlank(message = "Código de barras é obrigatório")
    @Size(max = 50)
    @Schema(description = "Código de barras do produto", example = "7891234567895")
    private String codigoBarras;

    @Schema(description = "Descrição opcional do produto", example = "Descrição breve do produto")
    private String descricao;

    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    @Schema(description = "Preço do produto", example = "19.90")
    private BigDecimal preco;

    @Min(value = 0, message = "Estoque não pode ser negativo")
    @Schema(description = "Quantidade em estoque", example = "10")
    private Integer estoque;

    @NotNull(message = "Fabricante é obrigatório")
    @Schema(description = "Identificador do fabricante associado", example = "1")
    private Long fabricanteId;

    public ProdutoDTO() {}

    // getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public void setEstoque(Integer estoque) {
        this.estoque = estoque;
    }

    public Long getFabricanteId() {
        return fabricanteId;
    }

    public void setFabricanteId(Long fabricanteId) {
        this.fabricanteId = fabricanteId;
    }
}
