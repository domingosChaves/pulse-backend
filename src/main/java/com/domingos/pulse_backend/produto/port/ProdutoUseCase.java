package com.domingos.pulse_backend.produto.port;

import com.domingos.pulse_backend.produto.Produto;
import com.domingos.pulse_backend.produto.ProdutoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ProdutoUseCase {
    Produto criar(ProdutoDTO dto);
    List<Produto> listar();
    Page<Produto> listar(Pageable pageable);
    Page<Produto> buscarPorNome(String nome, Pageable pageable);
    List<Produto> listarPorFabricante(Long fabricanteId);
    Page<Produto> listarPorFabricantePaged(Long fabricanteId, Pageable pageable);
    Produto buscarPorId(Long id);
    Produto atualizar(Long id, ProdutoDTO dto);
    void excluir(Long id);
    Map<String, List<Produto>> agruparPorFabricante();
}

