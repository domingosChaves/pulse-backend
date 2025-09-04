package com.domingos.pulse_backend.produto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/produtos")
@Validated
@Tag(name = "Produtos", description = "Operações para gerenciamento de produtos")
public class ProdutoController {

    private final com.domingos.pulse_backend.produto.port.ProdutoUseCase service;

    public ProdutoController(com.domingos.pulse_backend.produto.port.ProdutoUseCase service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar produto", description = "Cria um novo produto vinculado a um fabricante")
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoDTO dto) {
        Produto criado = service.criar(dto);
        ProdutoResponse resp = toResponse(criado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(resp, headers, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista todos os produtos; pode filtrar por fabricanteId")
    public List<ProdutoResponse> listar(@RequestParam(name = "fabricanteId", required = false) Long fabricanteId) {
        List<Produto> produtos = (fabricanteId == null) ? service.listar() : service.listarPorFabricante(fabricanteId);
        return produtos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/paged")
    @Operation(summary = "Listar produtos paginados", description = "Suporta filtros por nome e fabricanteId; utiliza parâmetros padrão de paginação do Spring (page, size, sort)")
    public Page<ProdutoResponse> listarPaged(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "fabricanteId", required = false) Long fabricanteId,
            Pageable pageable
    ) {
        Page<Produto> page;
        if (nome != null && !nome.isBlank()) {
            page = service.buscarPorNome(nome, pageable);
        } else if (fabricanteId != null) {
            page = service.listarPorFabricantePaged(fabricanteId, pageable);
        } else {
            page = service.listar(pageable);
        }
        return page.map(this::toResponse);
    }

    @GetMapping("/relatorio")
    @Operation(summary = "Relatório de produtos por fabricante", description = "Agrupa produtos por nome do fabricante (chave do mapa = nome do fabricante)")
    public Map<String, List<ProdutoResponse>> relatorioAgrupadoPorFabricante() {
        return service.agruparPorFabricante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(this::toResponse).collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return toResponse(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return toResponse(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir produto")
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }

    private ProdutoResponse toResponse(Produto p) {
        ProdutoResponse r = new ProdutoResponse();
        r.setId(p.getId());
        r.setNome(p.getNome());
        r.setCodigoBarras(p.getCodigoBarras());
        r.setDescricao(p.getDescricao());
        r.setPreco(p.getPreco());
        r.setEstoque(p.getEstoque());
        if (p.getFabricante() != null) {
            r.setFabricanteId(p.getFabricante().getId());
            r.setFabricanteNome(p.getFabricante().getNome());
        }
        return r;
    }
}
