package com.domingos.pulse_backend.bff;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteDTO;
import com.domingos.pulse_backend.produto.ProdutoDTO;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import com.domingos.pulse_backend.produto.Produto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BffService {

    private final com.domingos.pulse_backend.bff.client.FabricanteClient fabricanteClient;
    private final com.domingos.pulse_backend.bff.client.ProdutoClient produtoClient;

    public BffService(com.domingos.pulse_backend.bff.client.FabricanteClient fabricanteClient,
                      com.domingos.pulse_backend.bff.client.ProdutoClient produtoClient) {
        this.fabricanteClient = fabricanteClient;
        this.produtoClient = produtoClient;
    }

    // Fabricante
    public List<Fabricante> listarFabricantes() {
        return fabricanteClient.listar();
    }

    public Fabricante buscarFabricante(Long id) {
        return fabricanteClient.buscar(id);
    }

    public Fabricante criarFabricante(FabricanteDTO dto) {
        return fabricanteClient.criar(dto);
    }

    public Fabricante atualizarFabricante(Long id, FabricanteDTO dto) {
        return fabricanteClient.atualizar(id, dto);
    }

    public void excluirFabricante(Long id) {
        fabricanteClient.excluir(id);
    }

    // Produto
    public List<ProdutoResponse> listarProdutos() {
        return produtoClient.listar();
    }

    public ProdutoResponse buscarProduto(Long id) {
        return produtoClient.buscar(id);
    }

    public ProdutoResponse criarProduto(ProdutoDTO dto) {
        return produtoClient.criar(dto);
    }

    public ProdutoResponse atualizarProduto(Long id, ProdutoDTO dto) {
        return produtoClient.atualizar(id, dto);
    }

    public void excluirProduto(Long id) {
        produtoClient.excluir(id);
    }
}

