package com.example.authservice.handler;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<?> handleEntityExistsException(EntityExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessage = new StringBuilder("Validation errors: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append("Field '")
                        .append(error.getField())
                        .append("' - ")
                        .append(error.getDefaultMessage())
                        .append(". ")
        );
        return ResponseEntity.badRequest().body(errorMessage.toString().trim());
    }
}
