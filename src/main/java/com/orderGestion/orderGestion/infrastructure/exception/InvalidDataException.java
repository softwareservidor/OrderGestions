package com.orderGestion.orderGestion.infrastructure.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(final String message) {
        super(message);
    }
}
