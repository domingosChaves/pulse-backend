package com.domingos.pulse_backend.fabricante;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FabricanteServiceTest {

    @Mock
    private FabricanteRepository repository;

    @InjectMocks
    private FabricanteService service;

    private Fabricante fabricante;

    @BeforeEach
    void setUp() {
        fabricante = new Fabricante("ACME", "12345678000199", "Rua A", "(11)9999-9999", "João");
        fabricante.setId(1L);
    }

    @Test
    void criar_sucesso() {
        when(repository.findByCnpj(fabricante.getCnpj())).thenReturn(Optional.empty());
        when(repository.save(ArgumentMatchers.any(Fabricante.class))).thenAnswer(i -> {
            Fabricante f = i.getArgument(0);
            f.setId(1L);
            return f;
        });

        FabricanteDTO dto = new FabricanteDTO();
        dto.setNome(fabricante.getNome());
        dto.setCnpj(fabricante.getCnpj());

        Fabricante criado = service.criar(dto);

        assertNotNull(criado.getId());
        assertEquals("ACME", criado.getNome());
        verify(repository, times(1)).save(ArgumentMatchers.any(Fabricante.class));
    }

    @Test
    void criar_cnpjDuplicado_deveLancar() {
        when(repository.findByCnpj(fabricante.getCnpj())).thenReturn(Optional.of(fabricante));

        FabricanteDTO dto = new FabricanteDTO();
        dto.setNome(fabricante.getNome());
        dto.setCnpj(fabricante.getCnpj());

        assertThrows(IllegalArgumentException.class, () -> service.criar(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void buscarPorId_naoEncontrado_deveLancar() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void listar_retornaLista() {
        when(repository.findAll()).thenReturn(List.of(fabricante));
        List<Fabricante> list = service.listar();
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }

    @Test
    void excluir_deveChamarDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(fabricante));
        service.excluir(1L);
        verify(repository, times(1)).delete(fabricante);
    }
}

