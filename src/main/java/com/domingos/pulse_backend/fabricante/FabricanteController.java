package com.domingos.pulse_backend.fabricante;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fabricantes")
@Validated
@Tag(name = "Fabricantes", description = "Operações para gerenciamento de fabricantes")
public class FabricanteController {

    private final com.domingos.pulse_backend.fabricante.port.FabricanteUseCase service;

    public FabricanteController(com.domingos.pulse_backend.fabricante.port.FabricanteUseCase service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar fabricante")
    public ResponseEntity<Fabricante> criar(@Valid @RequestBody FabricanteDTO dto) {
        Fabricante criado = service.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(criado, headers, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar fabricantes")
    public List<Fabricante> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar fabricante por ID")
    public Fabricante buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fabricante")
    public Fabricante atualizar(@PathVariable Long id, @Valid @RequestBody FabricanteDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir fabricante")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
