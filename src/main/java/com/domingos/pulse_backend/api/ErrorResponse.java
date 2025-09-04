package com.domingos.pulse_backend.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "ErrorResponse", description = "Estrutura padrão de erro para respostas 4xx/5xx")
public class ErrorResponse {
    @Schema(description = "Mensagem de erro para o cliente", example = "Recurso não encontrado")
    private String error;

    @Schema(description = "Momento em que o erro ocorreu")
    private LocalDateTime timestamp;

    @Schema(description = "Caminho do endpoint invocado", example = "/api/produtos/99")
    private String path;

    public ErrorResponse() {}

    public ErrorResponse(String error, LocalDateTime timestamp, String path) {
        this.error = error;
        this.timestamp = timestamp;
        this.path = path;
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}

