package com.domingos.pulse_backend.produto;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByFabricanteId(Long fabricanteId);
}
