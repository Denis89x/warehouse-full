package dev.lebenkov.warehouse.api.util.exception;

public class InvalidValidationException extends RuntimeException {
    public InvalidValidationException(String message) {
        super(message);
    }
}
