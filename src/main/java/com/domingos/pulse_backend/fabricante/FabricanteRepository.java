package com.domingos.pulse_backend.fabricante;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FabricanteRepository extends JpaRepository<Fabricante, Long> {
    Optional<Fabricante> findByCnpj(String cnpj);
}

