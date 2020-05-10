package com.dataflow.example.exceptions;

public class InsertingIntoDatabaseException extends RuntimeException {
    public InsertingIntoDatabaseException(String message, Throwable throwable) {
        super(message, throwable);
    }
}