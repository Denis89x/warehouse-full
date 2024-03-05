package dev.lebenkov.warehouse.api.validation;

import dev.lebenkov.warehouse.api.util.exception.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onProductNotFoundException(ProductNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ProductTypeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onProductTypeNotFoundException(ProductTypeNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(StoreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onStoreNotFoundException(StoreNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(SupplierNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onSupplierNotFoundException(SupplierNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onOrderNotFoundException(OrderNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AccountAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onAccountExistsException(AccountAlreadyExistsException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onAccountNotFoundException(AccountNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmptyOrderCompositionException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String onEmptyOrderCompositionException(EmptyOrderCompositionException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InvalidValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String onValidationException(InvalidValidationException e) {
        return e.getMessage();
    }
}