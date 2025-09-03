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

@RestController
@RequestMapping("/api/bff")
public class BffController {

    private final BffService service;

    public BffController(BffService service) {
        this.service = service;
    }

    // Fabricante
    @GetMapping("/fabricantes")
    public List<Fabricante> listarFabricantes() {
        return service.listarFabricantes();
    }

    @GetMapping("/fabricantes/{id}")
    public Fabricante buscarFabricante(@PathVariable Long id) {
        return service.buscarFabricante(id);
    }

    @PostMapping("/fabricantes")
    public ResponseEntity<Fabricante> criarFabricante(@Valid @RequestBody FabricanteDTO dto) {
        Fabricante criado = service.criarFabricante(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(criado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/fabricantes/{id}")
    public Fabricante atualizarFabricante(@PathVariable Long id, @Valid @RequestBody FabricanteDTO dto) {
        return service.atualizarFabricante(id, dto);
    }

    @DeleteMapping("/fabricantes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirFabricante(@PathVariable Long id) {
        service.excluirFabricante(id);
    }

    // Produto
    @GetMapping("/produtos")
    public List<ProdutoResponse> listarProdutos() {
        return service.listarProdutos();
    }

    @GetMapping("/produtos/{id}")
    public ProdutoResponse buscarProduto(@PathVariable Long id) {
        return service.buscarProduto(id);
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoResponse> criarProduto(@Valid @RequestBody ProdutoDTO dto) {
        ProdutoResponse criado = service.criarProduto(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(criado, headers, HttpStatus.CREATED);
    }

    @PutMapping("/produtos/{id}")
    public ProdutoResponse atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return service.atualizarProduto(id, dto);
    }

    @DeleteMapping("/produtos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirProduto(@PathVariable Long id) {
        service.excluirProduto(id);
    }

    // NOVO: endpoint paginado via BFF
    @GetMapping("/produtos/paged")
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
    public Map<String, List<ProdutoResponse>> relatorioProdutos() {
        return service.relatorioProdutos();
    }
}
