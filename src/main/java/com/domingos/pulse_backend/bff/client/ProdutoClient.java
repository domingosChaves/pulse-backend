package com.domingos.pulse_backend.bff.client;

import com.domingos.pulse_backend.produto.ProdutoDTO;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import com.domingos.pulse_backend.produto.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "produto-client", url = "${bff.backend.url}")
public interface ProdutoClient {

    @PostMapping("/api/produtos")
    ProdutoResponse criar(@RequestBody ProdutoDTO dto);

    @GetMapping("/api/produtos")
    List<ProdutoResponse> listar();

    @GetMapping("/api/produtos/{id}")
    ProdutoResponse buscar(@PathVariable("id") Long id);

    @PutMapping("/api/produtos/{id}")
    ProdutoResponse atualizar(@PathVariable("id") Long id, @RequestBody ProdutoDTO dto);

    @DeleteMapping("/api/produtos/{id}")
    void excluir(@PathVariable("id") Long id);

    // Paged endpoint: aceita parâmetros opcionais nome, fabricanteId, page, size, sort
    @GetMapping("/api/produtos/paged")
    PageResponse<ProdutoResponse> paged(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "fabricanteId", required = false) Long fabricanteId,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort", required = false) String sort
    );

    // Relatorio agrupado por nome do fabricante
    @GetMapping("/api/produtos/relatorio")
    Map<String, List<ProdutoResponse>> relatorio();
}
