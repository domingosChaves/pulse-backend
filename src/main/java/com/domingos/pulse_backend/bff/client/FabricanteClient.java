package com.domingos.pulse_backend.bff.client;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "fabricante-client", url = "${bff.backend.url}", configuration = com.domingos.pulse_backend.config.FeignConfig.class)
public interface FabricanteClient {

    @PostMapping("/api/fabricantes")
    Fabricante criar(@RequestBody FabricanteDTO dto);

    @GetMapping("/api/fabricantes")
    List<Fabricante> listar();

    @GetMapping("/api/fabricantes/{id}")
    Fabricante buscar(@PathVariable("id") Long id);

    @PutMapping("/api/fabricantes/{id}")
    Fabricante atualizar(@PathVariable("id") Long id, @RequestBody FabricanteDTO dto);

    @DeleteMapping("/api/fabricantes/{id}")
    void excluir(@PathVariable("id") Long id);
}
