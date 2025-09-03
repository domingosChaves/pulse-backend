package com.domingos.pulse_backend.bff.client;

import com.domingos.pulse_backend.produto.ProdutoDTO;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}

