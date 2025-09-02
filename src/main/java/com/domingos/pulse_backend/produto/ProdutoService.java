package com.domingos.pulse_backend.produto;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteRepository;
import com.domingos.pulse_backend.fabricante.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FabricanteRepository fabricanteRepository;

    public ProdutoService(ProdutoRepository produtoRepository, FabricanteRepository fabricanteRepository) {
        this.produtoRepository = produtoRepository;
        this.fabricanteRepository = fabricanteRepository;
    }

    public Produto criar(ProdutoDTO dto) {
        produtoRepository.findByCodigoBarras(dto.getCodigoBarras()).ifPresent(p -> {
            throw new IllegalArgumentException("Código de barras já cadastrado");
        });
        Fabricante fabricante = fabricanteRepository.findById(dto.getFabricanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com id: " + dto.getFabricanteId()));
        Produto p = new Produto(dto.getNome(), dto.getCodigoBarras(), dto.getDescricao(), dto.getPreco(), dto.getEstoque(), fabricante);
        return produtoRepository.save(p);
    }

    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    public Produto atualizar(Long id, ProdutoDTO dto) {
        Produto existente = buscarPorId(id);
        if (!existente.getCodigoBarras().equals(dto.getCodigoBarras())) {
            produtoRepository.findByCodigoBarras(dto.getCodigoBarras()).ifPresent(p -> {
                throw new IllegalArgumentException("Código de barras já cadastrado");
            });
            existente.setCodigoBarras(dto.getCodigoBarras());
        }
        if (!existente.getFabricante().getId().equals(dto.getFabricanteId())) {
            Fabricante fabricante = fabricanteRepository.findById(dto.getFabricanteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com id: " + dto.getFabricanteId()));
            existente.setFabricante(fabricante);
        }
        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setPreco(dto.getPreco());
        existente.setEstoque(dto.getEstoque());
        return produtoRepository.save(existente);
    }

    public void excluir(Long id) {
        Produto existente = buscarPorId(id);
        produtoRepository.delete(existente);
    }
}

