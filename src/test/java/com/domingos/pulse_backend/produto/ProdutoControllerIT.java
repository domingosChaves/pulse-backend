package com.domingos.pulse_backend.produto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listarProdutos_ok() throws Exception {
        mockMvc.perform(get("/api/produtos").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void listarPorFabricante_ok() throws Exception {
        // fabricante 1 tem 2 produtos no data.sql
        mockMvc.perform(get("/api/produtos").param("fabricanteId", "1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].fabricanteId", is(1)));
    }

    @Test
    void paged_buscaPorNome_ok() throws Exception {
        mockMvc.perform(get("/api/produtos/paged").param("nome", "Produto").param("page", "0").param("size", "2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(lessThanOrEqualTo(2))))
                .andExpect(jsonPath("$.totalElements", greaterThanOrEqualTo(1)));
    }

    @Test
    void relatorioAgrupadoPorFabricante_ok() throws Exception {
        mockMvc.perform(get("/api/produtos/relatorio").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath(".['ACME Indústria']", hasSize(2)))
                .andExpect(jsonPath(".['Beta Ltda']", hasSize(1)));
    }

    @Test
    void criarProduto_validacaoErro() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("");
        dto.setCodigoBarras("");
        dto.setFabricanteId(null);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome", notNullValue()))
                .andExpect(jsonPath("$.codigoBarras", notNullValue()))
                .andExpect(jsonPath("$.fabricanteId", notNullValue()));
    }

    @Test
    void criarProduto_sucesso_created() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Produto Novo");
        dto.setCodigoBarras("7891234567898");
        dto.setDescricao("Desc");
        dto.setPreco(new BigDecimal("11.50"));
        dto.setEstoque(7);
        dto.setFabricanteId(1L);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.fabricanteNome", is("ACME Indústria")));
    }

    @Test
    void criarProduto_codigoBarrasDuplicado_badRequest() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Outro");
        dto.setCodigoBarras("7891234567895"); // já existe via data.sql
        dto.setFabricanteId(1L);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", containsString("Código de barras")));
    }

    @Test
    void buscarProduto_naoEncontrado_notFound() throws Exception {
        mockMvc.perform(get("/api/produtos/99999").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("não encontrado")));
    }
}

