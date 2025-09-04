package com.domingos.pulse_backend.fabricante;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FabricanteControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarFabricantes_ok() throws Exception {
        mockMvc.perform(get("/api/fabricantes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void criarFabricante_validacaoErro() throws Exception {
        FabricanteDTO dto = new FabricanteDTO();
        dto.setNome(""); // inválido
        dto.setCnpj(""); // inválido

        mockMvc.perform(post("/api/fabricantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome", notNullValue()))
                .andExpect(jsonPath("$.cnpj", notNullValue()));
    }

    @Test
    void criarFabricante_cnpjDuplicado_badRequest() throws Exception {
        FabricanteDTO dto = new FabricanteDTO();
        dto.setNome("Outro ACME");
        dto.setCnpj("12345678000199"); // já existe via data.sql

        mockMvc.perform(post("/api/fabricantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("CNPJ")));
    }

    @Test
    void buscarFabricante_naoEncontrado_notFound() throws Exception {
        mockMvc.perform(get("/api/fabricantes/9999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("não encontrado")));
    }

    @Test
    void criarFabricante_sucesso_createdComLocation() throws Exception {
        FabricanteDTO dto = new FabricanteDTO();
        dto.setNome("Novo Fab");
        dto.setCnpj("11222333000144");
        dto.setEndereco("Rua C, 300");
        dto.setTelefone("(11)9777-0003");
        dto.setContato("Ana");

        mockMvc.perform(post("/api/fabricantes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nome", is("Novo Fab")));
    }
}

