package com.example.bookservice.exception;

public class BookValidationException extends RuntimeException {
    public BookValidationException(String message){
        super(message);
    }
}
