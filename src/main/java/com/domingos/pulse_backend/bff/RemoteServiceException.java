package com.domingos.pulse_backend.bff;

/**
 * Exceção genérica para erros retornados por serviços remotos
 * consumidos via Feign. Utilize-a para encapsular respostas 4xx/5xx
 * que não possuam mapeamento específico na aplicação.
 */
public class RemoteServiceException extends RuntimeException {
    public RemoteServiceException(String message) {
        super(message);
    }

    public RemoteServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
