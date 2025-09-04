package com.domingos.pulse_backend.produto;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.port.FabricantePort;
import com.domingos.pulse_backend.produto.port.ProdutoPort;
import com.domingos.pulse_backend.produto.port.ProdutoUseCase;
import com.domingos.pulse_backend.fabricante.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProdutoService implements ProdutoUseCase {

    private final ProdutoPort produtoPort;
    private final FabricantePort fabricantePort;

    public ProdutoService(ProdutoPort produtoPort, FabricantePort fabricantePort) {
        this.produtoPort = produtoPort;
        this.fabricantePort = fabricantePort;
    }

    @Override
    public Produto criar(ProdutoDTO dto) {
        produtoPort.findByCodigoBarras(dto.getCodigoBarras()).ifPresent(p -> {
            throw new IllegalArgumentException("Código de barras já cadastrado");
        });
        Fabricante fabricante = fabricantePort.findById(dto.getFabricanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com id: " + dto.getFabricanteId()));
        Produto p = new Produto(dto.getNome(), dto.getCodigoBarras(), dto.getDescricao(), dto.getPreco(), dto.getEstoque(), fabricante);
        return produtoPort.save(p);
    }

    @Override
    public List<Produto> listar() {
        return produtoPort.findAll();
    }

    @Override
    public Page<Produto> listar(Pageable pageable) {
        return produtoPort.findAll(pageable);
    }

    @Override
    public Page<Produto> buscarPorNome(String nome, Pageable pageable) {
        return produtoPort.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Override
    public List<Produto> listarPorFabricante(Long fabricanteId) {
        return produtoPort.findByFabricanteId(fabricanteId);
    }

    @Override
    public Page<Produto> listarPorFabricantePaged(Long fabricanteId, Pageable pageable) {
        return produtoPort.findByFabricanteId(fabricanteId, pageable);
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtoPort.findById(id).orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com id: " + id));
    }

    @Override
    public Produto atualizar(Long id, ProdutoDTO dto) {
        Produto existente = buscarPorId(id);
        if (!existente.getCodigoBarras().equals(dto.getCodigoBarras())) {
            produtoPort.findByCodigoBarras(dto.getCodigoBarras()).ifPresent(p -> {
                throw new IllegalArgumentException("Código de barras já cadastrado");
            });
            existente.setCodigoBarras(dto.getCodigoBarras());
        }
        if (!existente.getFabricante().getId().equals(dto.getFabricanteId())) {
            Fabricante fabricante = fabricantePort.findById(dto.getFabricanteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fabricante não encontrado com id: " + dto.getFabricanteId()));
            existente.setFabricante(fabricante);
        }
        existente.setNome(dto.getNome());
        existente.setDescricao(dto.getDescricao());
        existente.setPreco(dto.getPreco());
        existente.setEstoque(dto.getEstoque());
        return produtoPort.save(existente);
    }

    @Override
    public void excluir(Long id) {
        Produto existente = buscarPorId(id);
        produtoPort.delete(existente);
    }

    @Override
    public Map<String, List<Produto>> agruparPorFabricante() {
        return produtoPort.findAll().stream()
                .collect(Collectors.groupingBy(p -> {
                    Fabricante f = p.getFabricante();
                    return (f != null && f.getNome() != null) ? f.getNome() : "<sem-fabricante>";
                }));
    }
}
