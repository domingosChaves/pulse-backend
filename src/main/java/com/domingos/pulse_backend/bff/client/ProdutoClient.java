package com.domingos.pulse_backend.bff.client;

import com.domingos.pulse_backend.produto.ProdutoDTO;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import com.domingos.pulse_backend.produto.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Cliente Feign para consumir os endpoints de Produtos do backend.
 * A URL base é configurada via propriedade bff.backend.url.
 */
@FeignClient(name = "produto-client", url = "${bff.backend.url}")
public interface ProdutoClient {

    /** Cria um novo produto. */
    @PostMapping("/api/produtos")
    ProdutoResponse criar(@RequestBody ProdutoDTO dto);

    /** Lista todos os produtos. */
    @GetMapping("/api/produtos")
    List<ProdutoResponse> listar();

    /** Busca um produto pelo ID. */
    @GetMapping("/api/produtos/{id}")
    ProdutoResponse buscar(@PathVariable("id") Long id);

    /** Atualiza um produto existente. */
    @PutMapping("/api/produtos/{id}")
    ProdutoResponse atualizar(@PathVariable("id") Long id, @RequestBody ProdutoDTO dto);

    /** Exclui um produto pelo ID. */
    @DeleteMapping("/api/produtos/{id}")
    void excluir(@PathVariable("id") Long id);

    /**
     * Lista produtos paginados com filtros opcionais.
     * Parâmetros: nome, fabricanteId, page, size, sort
     */
    @GetMapping("/api/produtos/paged")
    PageResponse<ProdutoResponse> paged(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "fabricanteId", required = false) Long fabricanteId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", required = false) String sort
    );

    /** Retorna relatório agrupado por nome do fabricante. */
    @GetMapping("/api/produtos/relatorio")
    Map<String, List<ProdutoResponse>> relatorio();
}
