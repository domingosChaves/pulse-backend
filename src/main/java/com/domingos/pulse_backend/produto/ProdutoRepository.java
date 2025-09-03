package com.domingos.pulse_backend.produto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByFabricanteId(Long fabricanteId);

    // paginação e busca
    Page<Produto> findAll(Pageable pageable);
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Produto> findByFabricanteId(Long fabricanteId, Pageable pageable);
}
