package dev.lebenkov.warehouse.api.util.exception;

public class EmptyOrderCompositionException extends RuntimeException {
    public EmptyOrderCompositionException(String message) {
        super(message);
    }
}
