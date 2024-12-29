package com.example.apigateway.exception;

public class BookValidationException extends RuntimeException {
    public BookValidationException(String message){
        super(message);
    }
}
