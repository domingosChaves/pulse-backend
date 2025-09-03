package com.domingos.pulse_backend.produto;

import com.domingos.pulse_backend.fabricante.Fabricante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private com.domingos.pulse_backend.produto.port.ProdutoPort produtoPort;

    @Mock
    private com.domingos.pulse_backend.fabricante.port.FabricantePort fabricantePort;

    @InjectMocks
    private ProdutoService service;

    private Fabricante fabricante1;
    private Fabricante fabricante2;
    private Produto produto;

    @BeforeEach
    void setUp() {
        fabricante1 = new Fabricante("ACME", "12345678000199", "Rua A", "(11)9999-9999", "João");
        fabricante1.setId(1L);
        fabricante2 = new Fabricante("Beta", "98765432000188", "Rua B", "(11)9888-8888", "Maria");
        fabricante2.setId(2L);

        produto = new Produto("Produto X", "0123456789012", "Desc", new BigDecimal("10.0"), 5, fabricante1);
        produto.setId(1L);
    }

    @Test
    void criar_sucesso() {
        when(produtoPort.findByCodigoBarras(produto.getCodigoBarras())).thenReturn(Optional.empty());
        when(fabricantePort.findById(fabricante1.getId())).thenReturn(Optional.of(fabricante1));
        when(produtoPort.save(ArgumentMatchers.any(Produto.class))).thenAnswer(i -> {
            Produto p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome(produto.getNome());
        dto.setCodigoBarras(produto.getCodigoBarras());
        dto.setFabricanteId(fabricante1.getId());
        dto.setPreco(produto.getPreco());
        dto.setEstoque(produto.getEstoque());

        Produto criado = service.criar(dto);

        assertNotNull(criado.getId());
        assertEquals("Produto X", criado.getNome());
        verify(produtoPort, times(1)).save(ArgumentMatchers.any(Produto.class));
    }

    @Test
    void criar_codigoBarrasDuplicado_deveLancar() {
        when(produtoPort.findByCodigoBarras(produto.getCodigoBarras())).thenReturn(Optional.of(produto));

        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome(produto.getNome());
        dto.setCodigoBarras(produto.getCodigoBarras());
        dto.setFabricanteId(fabricante1.getId());

        assertThrows(IllegalArgumentException.class, () -> service.criar(dto));
        verify(produtoPort, never()).save(any());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancar() {
        when(produtoPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(com.domingos.pulse_backend.fabricante.ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void listar_retornaLista() {
        when(produtoPort.findAll()).thenReturn(List.of(produto));
        List<Produto> list = service.listar();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void excluir_deveChamarDelete() {
        when(produtoPort.findById(1L)).thenReturn(Optional.of(produto));
        service.excluir(1L);
        verify(produtoPort, times(1)).delete(produto);
    }

    @Test
    void atualizar_alterarCodigoBarrasEFabricante() {
        when(produtoPort.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoPort.findByCodigoBarras("9998887776665")).thenReturn(Optional.empty());
        when(fabricantePort.findById(2L)).thenReturn(Optional.of(fabricante2));
        when(produtoPort.save(ArgumentMatchers.any(Produto.class))).thenAnswer(i -> i.getArgument(0));

        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Produto X atualizado");
        dto.setCodigoBarras("9998887776665");
        dto.setFabricanteId(2L);
        dto.setPreco(new BigDecimal("12.5"));
        dto.setEstoque(10);

        Produto atualizado = service.atualizar(1L, dto);

        assertEquals("9998887776665", atualizado.getCodigoBarras());
        assertEquals(2L, atualizado.getFabricante().getId());
        assertEquals(new BigDecimal("12.5"), atualizado.getPreco());
    }
}
