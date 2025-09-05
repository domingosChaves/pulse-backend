package com.domingos.pulse_backend.produto.port;

import com.domingos.pulse_backend.produto.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProdutoPort {
    Produto save(Produto produto);
    List<Produto> findAll();
    Optional<Produto> findById(Long id);
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByFabricanteId(Long fabricanteId);
    void delete(Produto produto);

    // pagination support
    Page<Produto> findAll(Pageable pageable);
    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Produto> findByFabricanteId(Long fabricanteId, Pageable pageable);
}
