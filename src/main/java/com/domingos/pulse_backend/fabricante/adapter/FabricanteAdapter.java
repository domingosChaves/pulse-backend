package com.domingos.pulse_backend.fabricante.adapter;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteRepository;
import com.domingos.pulse_backend.fabricante.port.FabricantePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FabricanteAdapter implements FabricantePort {

    private final FabricanteRepository repository;

    public FabricanteAdapter(FabricanteRepository repository) {
        this.repository = repository;
    }

    @Override
    public Fabricante save(Fabricante fabricante) {
        return repository.save(fabricante);
    }

    @Override
    public List<Fabricante> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Fabricante> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Fabricante> findByCnpj(String cnpj) {
        return repository.findByCnpj(cnpj);
    }

    @Override
    public void delete(Fabricante fabricante) {
        repository.delete(fabricante);
    }
}

