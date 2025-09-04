package com.domingos.pulse_backend.bff.client;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Cliente Feign para consumir os endpoints de Fabricantes do backend.
 * A URL base é configurada via propriedade bff.backend.url.
 */
@FeignClient(name = "fabricante-client", url = "${bff.backend.url}", configuration = com.domingos.pulse_backend.config.FeignConfig.class)
public interface FabricanteClient {

    /** Cria um novo fabricante. */
    @PostMapping("/api/fabricantes")
    Fabricante criar(@RequestBody FabricanteDTO dto);

    /** Lista todos os fabricantes. */
    @GetMapping("/api/fabricantes")
    List<Fabricante> listar();

    /** Busca um fabricante pelo ID. */
    @GetMapping("/api/fabricantes/{id}")
    Fabricante buscar(@PathVariable("id") Long id);

    /** Atualiza um fabricante existente. */
    @PutMapping("/api/fabricantes/{id}")
    Fabricante atualizar(@PathVariable("id") Long id, @RequestBody FabricanteDTO dto);

    /** Exclui um fabricante pelo ID. */
    @DeleteMapping("/api/fabricantes/{id}")
    void excluir(@PathVariable("id") Long id);
}
