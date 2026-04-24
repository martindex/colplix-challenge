package com.colpix.challenge.exception;

/**
 * Excepción base para errores de lógica de negocio.
 */
public class BaseException extends RuntimeException {
    public BaseException(String message) {
        super(message);
    }
}
