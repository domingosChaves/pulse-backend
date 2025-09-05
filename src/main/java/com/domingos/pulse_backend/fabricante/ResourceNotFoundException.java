package com.domingos.pulse_backend.fabricante;

/**
 * Exceção para sinalizar que um recurso (entidade) não foi encontrado.
 * Deve ser utilizada em operações de busca/atualização/exclusão quando o ID não existe.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
