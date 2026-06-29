package com.ingressos.api.exception;

/**
 * Lançada quando um registro não é localizado pelo id informado.
 * Sempre tratada como HTTP 404 pelo GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
