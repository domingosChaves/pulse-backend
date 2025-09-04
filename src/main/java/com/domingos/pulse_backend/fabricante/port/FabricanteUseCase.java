package com.domingos.pulse_backend.fabricante.port;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteDTO;

import java.util.List;

public interface FabricanteUseCase {
    Fabricante criar(FabricanteDTO dto);
    List<Fabricante> listar();
    Fabricante buscarPorId(Long id);
    Fabricante atualizar(Long id, FabricanteDTO dto);
    void excluir(Long id);
}

