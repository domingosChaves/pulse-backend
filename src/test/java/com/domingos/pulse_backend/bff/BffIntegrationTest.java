package com.domingos.pulse_backend.bff;

import com.domingos.pulse_backend.bff.client.ProdutoClient;
import com.domingos.pulse_backend.bff.client.FabricanteClient;
import com.domingos.pulse_backend.produto.PageResponse;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import com.domingos.pulse_backend.fabricante.Fabricante;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "tester", roles = {"USER"})
public class BffIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoClient produtoClient;

    @MockBean
    private FabricanteClient fabricanteClient;

    @Test
    void pagedEndpoint_delegatesToProdutoClient() throws Exception {
        ProdutoResponse p1 = new ProdutoResponse(1L, "P1", "111", "d1", new BigDecimal("10.0"), 5, 1L, "F1");
        ProdutoResponse p2 = new ProdutoResponse(2L, "P2", "222", "d2", new BigDecimal("20.0"), 3, 1L, "F1");
        PageResponse<ProdutoResponse> page = new PageResponse<>();
        page.setContent(List.of(p1, p2));
        page.setTotalElements(2);
        page.setTotalPages(1);
        page.setNumber(0);
        page.setSize(2);

        when(produtoClient.paged(eq("P"), isNull(), eq(0), eq(2), isNull())).thenReturn(page);

        mockMvc.perform(get("/api/bff/produtos/paged").param("nome", "P").param("page","0").param("size","2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.content[0].nome", is("P1")));

        verify(produtoClient, times(1)).paged(eq("P"), isNull(), eq(0), eq(2), isNull());
    }

    @Test
    void relatorioEndpoint_returnsGroupedMap() throws Exception {
        ProdutoResponse p1 = new ProdutoResponse(1L, "P1", "111", "d1", new BigDecimal("10.0"), 5, 1L, "ACME");
        ProdutoResponse p2 = new ProdutoResponse(2L, "P2", "222", "d2", new BigDecimal("20.0"), 3, 1L, "ACME");
        ProdutoResponse p3 = new ProdutoResponse(3L, "P3", "333", "d3", new BigDecimal("5.0"), 10, 2L, "Beta");
        Map<String, List<ProdutoResponse>> map = new HashMap<>();
        map.put("ACME", List.of(p1, p2));
        map.put("Beta", List.of(p3));

        when(produtoClient.relatorio()).thenReturn(map);

        mockMvc.perform(get("/api/bff/produtos/relatorio").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ACME", hasSize(2)))
                .andExpect(jsonPath("$.Beta", hasSize(1)))
                .andExpect(jsonPath("$.ACME[0].nome", is("P1")));

        verify(produtoClient, times(1)).relatorio();
    }

    @Test
    void listarFabricantes_delegatesToFabricanteClient() throws Exception {
        Fabricante f1 = new Fabricante("ACME", "12345678000199", "Rua A", "(11)9999-0001", "Joao");
        f1.setId(1L);
        when(fabricanteClient.listar()).thenReturn(List.of(f1));

        mockMvc.perform(get("/api/bff/fabricantes").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome", is("ACME")));

        verify(fabricanteClient, times(1)).listar();
    }
}
