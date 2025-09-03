package com.domingos.pulse_backend.produto.adapter;

import com.domingos.pulse_backend.produto.Produto;
import com.domingos.pulse_backend.produto.ProdutoRepository;
import com.domingos.pulse_backend.produto.port.ProdutoPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProdutoAdapter implements ProdutoPort {

    private final ProdutoRepository repository;

    public ProdutoAdapter(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Produto save(Produto produto) {
        return repository.save(produto);
    }

    @Override
    public List<Produto> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Produto> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Produto> findByCodigoBarras(String codigoBarras) {
        return repository.findByCodigoBarras(codigoBarras);
    }

    @Override
    public List<Produto> findByFabricanteId(Long fabricanteId) {
        return repository.findByFabricanteId(fabricanteId);
    }

    @Override
    public void delete(Produto produto) {
        repository.delete(produto);
    }

    // pagination and search delegates
    @Override
    public Page<Produto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Override
    public Page<Produto> findByFabricanteId(Long fabricanteId, Pageable pageable) {
        return repository.findByFabricanteId(fabricanteId, pageable);
    }
}

