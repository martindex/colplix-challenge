package com.colpix.challenge.exception;

/**
 * Excepción lanzada cuando hay un conflicto en la lógica de negocio (HTTP 409).
 */
public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message);
    }
}
