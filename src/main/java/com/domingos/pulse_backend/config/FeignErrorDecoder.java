package com.domingos.pulse_backend.config;

import com.domingos.pulse_backend.fabricante.ResourceNotFoundException;
import com.domingos.pulse_backend.bff.RemoteServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

/**
 * Tradutor de erros do Feign para exceções da aplicação.
 * Converte códigos HTTP comuns (400/404) em exceções específicas e
 * encapsula outros erros 4xx/5xx em RemoteServiceException.
 */
public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String message = extractMessage(response);
        if (status == 404) {
            return new ResourceNotFoundException(message != null ? message : "Recurso não encontrado");
        }
        if (status == 400) {
            return new IllegalArgumentException(message != null ? message : "Requisição inválida");
        }
        // Para outros erros 4xx/5xx, encapsula em RemoteServiceException
        return new RemoteServiceException("Erro no serviço remoto: status=" + status + " mensagem=" + (message != null ? message : ""));
    }

    private String extractMessage(Response response) {
        try {
            if (response.body() == null) return null;
            // lê o corpo da resposta (limitado ao tamanho padrão do buffer)
            byte[] bodyBytes = response.body().asInputStream().readAllBytes();
            if (bodyBytes.length == 0) return null;
            return new String(bodyBytes);
        } catch (IOException e) {
            return null;
        }
    }
}
