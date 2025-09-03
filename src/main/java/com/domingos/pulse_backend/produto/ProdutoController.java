package com.domingos.pulse_backend.produto;

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
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoDTO dto) {
        Produto criado = service.criar(dto);
        ProdutoResponse resp = toResponse(criado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(criado.getId()).toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(location);
        return new ResponseEntity<>(resp, headers, HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProdutoResponse> listar(@RequestParam(name = "fabricanteId", required = false) Long fabricanteId) {
        List<Produto> produtos = (fabricanteId == null) ? service.listar() : service.listarPorFabricante(fabricanteId);
        return produtos.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/paged")
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
    public Map<String, List<ProdutoResponse>> relatorioAgrupadoPorFabricante() {
        return service.agruparPorFabricante().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(this::toResponse).collect(Collectors.toList())));
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscar(@PathVariable Long id) {
        return toResponse(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        return toResponse(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
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
