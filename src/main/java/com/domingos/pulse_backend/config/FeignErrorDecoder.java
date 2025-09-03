package com.domingos.pulse_backend.config;

import com.domingos.pulse_backend.fabricante.ResourceNotFoundException;
import com.domingos.pulse_backend.bff.RemoteServiceException;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String message = extractMessage(response);
        if (status == 404) {
            return new ResourceNotFoundException(message != null ? message : "Recurso nao encontrado");
        }
        if (status == 400) {
            return new IllegalArgumentException(message != null ? message : "Requisicao invalida");
        }
        // For other 5xx/4xx errors, wrap into a RemoteServiceException
        return new RemoteServiceException("Remote service error: status=" + status + " message=" + (message != null ? message : ""));
    }

    private String extractMessage(Response response) {
        try {
            if (response.body() == null) return null;
            // read up to some bytes
            byte[] bodyBytes = response.body().asInputStream().readAllBytes();
            if (bodyBytes.length == 0) return null;
            return new String(bodyBytes);
        } catch (IOException e) {
            return null;
        }
    }
}

