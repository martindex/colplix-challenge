package com.colpix.challenge.exception;

/**
 * Excepción lanzada cuando un recurso no es encontrado (HTTP 404).
 */
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
