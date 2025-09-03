package com.domingos.pulse_backend.produto.port;

import com.domingos.pulse_backend.produto.Produto;

import java.util.List;
import java.util.Optional;

public interface ProdutoPort {
    Produto save(Produto produto);
    List<Produto> findAll();
    Optional<Produto> findById(Long id);
    Optional<Produto> findByCodigoBarras(String codigoBarras);
    List<Produto> findByFabricanteId(Long fabricanteId);
    void delete(Produto produto);
}
