package com.domingos.pulse_backend.fabricante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO para criação/atualização de Fabricante
public class FabricanteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255)
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 20)
    private String cnpj;

    private String endereco;
    private String telefone;
    private String contato;

    public FabricanteDTO() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}

