package com.domingos.pulse_backend.fabricante.port;

import com.domingos.pulse_backend.fabricante.Fabricante;

import java.util.List;
import java.util.Optional;

public interface FabricantePort {
    Fabricante save(Fabricante fabricante);
    List<Fabricante> findAll();
    Optional<Fabricante> findById(Long id);
    Optional<Fabricante> findByCnpj(String cnpj);
    void delete(Fabricante fabricante);
}

