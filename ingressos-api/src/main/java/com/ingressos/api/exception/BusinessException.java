package com.ingressos.api.exception;

/**
 * Lançada quando uma regra de negócio é violada
 * (ex: capacidade máxima do evento/local atingida, limite de ingressos por usuário).
 * Sempre tratada como HTTP 400 pelo GlobalExceptionHandler.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
