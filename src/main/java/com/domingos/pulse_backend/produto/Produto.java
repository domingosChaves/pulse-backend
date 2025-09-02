package com.domingos.pulse_backend.produto;

import com.domingos.pulse_backend.fabricante.Fabricante;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos", uniqueConstraints = {@UniqueConstraint(columnNames = "codigo_barras")})
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "codigo_barras", nullable = false, unique = true)
    private String codigoBarras;

    private String descricao;

    private BigDecimal preco;

    private Integer estoque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fabricante_id", nullable = false)
    private Fabricante fabricante;

    public Produto() {}

    public Produto(String nome, String codigoBarras, String descricao, BigDecimal preco, Integer estoque, Fabricante fabricante) {
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.fabricante = fabricante;
    }

    // getters e setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }
}

