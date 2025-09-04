package com.domingos.pulse_backend.bff;

import com.domingos.pulse_backend.fabricante.Fabricante;
import com.domingos.pulse_backend.fabricante.FabricanteDTO;
import com.domingos.pulse_backend.produto.ProdutoDTO;
import com.domingos.pulse_backend.produto.ProdutoResponse;
import com.domingos.pulse_backend.produto.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bff")
@Tag(name = "BFF", description = "Orquestra endpoints para Fabricantes e Produtos via Feign")
public class BffController {

    private final BffService service;

    public BffController(BffService service) {
        this.service = service;
    }

    // Fabricante
    @GetMapping("/fabricantes")
    @Operation(summary = "Listar fabricantes (via BFF)")
    public List<Fabricante> listarFabricantes() {
        return service.listarFabricantes();
    }

    @GetMapping("/fabricantes/{id}")
    @Operation(summary = "Buscar fabricante por ID (via BFF)")
    public Fabricante buscarFabricante(@PathVariable Long id) {
        return service.buscarFabricante(id);
    }

    @PostMapping("/fabricantes")
    @Operation(summary = "Criar fabricante (via BFF)")
    public ResponseEntity<Fabricante> criarFabricante(@Valid @RequestBody FabricanteDTO dto) {
        Fabricante criado = service.criarFabricante(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(criado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/fabricantes/{id}")
    @Operation(summary = "Atualizar fabricante (via BFF)")
    public Fabricante atualizarFabricante(@PathVariable Long id, @Valid @RequestBody FabricanteDTO dto) {
        return service.atualizarFabricante(id, dto);
    }

    @DeleteMapping("/fabricantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir fabricante (via BFF)")
    public void excluirFabricante(@PathVariable Long id) {
        service.excluirFabricante(id);
    }

    // Produto
    @GetMapping("/produtos")
    @Operation(summary = "Listar produtos (via BFF)")
    public List<ProdutoResponse> listarProdutos() {
        return service.listarProdutos();
    }

    @GetMapping("/produtos/{id}")
    @Operation(summary = "Buscar produto por ID (via BFF)")
    public ProdutoResponse buscarProduto(@PathVariable Long id) {
        return service.buscarProduto(id);
    }

    @PostMapping("/produtos")
    @Operation(summary = "Criar produto (via BFF)")
    public ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponse criado = service.criarProduto(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(criado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/produtos/{id}")
    @Operation(summary = "Atualizar produto (via BFF)")
    public ProdutoResponse atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return service.atualizarProduto(id, dto);
    }

    @DeleteMapping("/produtos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir produto (via BFF)")
    public void excluirProduto(@PathVariable Long id) {
        service.excluirProduto(id);
    }

    // NOVO: endpoint paginado via BFF
    @GetMapping("/produtos/paged")
    @Operation(summary = "Listar produtos paginados (via BFF)", description = "Proxy para paginação e filtros de produtos: nome, fabricanteId, page, size, sort")
    public PageResponse<ProdutoResponse> listarProdutosPaged(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "fabricanteId", required = false) Long fabricanteId,
            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false) String sort
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.listarProdutosPaged(nome, fabricanteId, page, size, sort);
    }

    // NOVO: relatório via BFF
    @GetMapping("/produtos/relatorio")
    @Operation(summary = "Relatório de produtos por fabricante (via BFF)", description = "Agrupa produtos por nome do fabricante (chave do mapa = nome do fabricante)")
    public Map<String, List<ProdutoResponse>> relatorioProdutos() {
        return service.relatorioProdutos();
    }
}
