package com.domingos.pulse_backend.fabricante;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.domingos.pulse_backend.fabricante.validation.CNPJ;
import io.swagger.v3.oas.annotations.media.Schema;

// DTO para criação/atualização de Fabricante
@Schema(name = "FabricanteDTO", description = "Payload para criação/atualização de fabricante")
public class FabricanteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 255)
    @Schema(description = "Nome do fabricante", example = "ACME Indústria")
    private String nome;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 20)
    @CNPJ(message = "CNPJ inválido")
    @Schema(description = "CNPJ do fabricante (apenas números)", example = "12345678000199")
    private String cnpj;

    @Schema(description = "Endereço opcional do fabricante", example = "Rua A, 100")
    private String endereco;
    @Schema(description = "Telefone de contato", example = "(11)9999-0001")
    private String telefone;
    @Schema(description = "Nome do contato", example = "João")
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
