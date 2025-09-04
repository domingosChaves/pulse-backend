package com.domingos.pulse_backend.produto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PageResponse", description = "Estrutura de resposta paginada")
public class PageResponse<T> {
    @Schema(description = "Conteúdo da página")
    private List<T> content;
    @Schema(description = "Total de elementos")
    private long totalElements;
    @Schema(description = "Total de páginas")
    private int totalPages;
    @Schema(description = "Número da página (0-based)")
    private int page;
    @Schema(description = "Tamanho da página")
    private int size;
    @Schema(description = "Ordenação aplicada (ex.: nome,asc)")
    private String sort;

    public PageResponse() {}

    public List<T> getContent() { return content; }
    public void setContent(List<T> content) { this.content = content; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
}
