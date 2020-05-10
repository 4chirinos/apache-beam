package com.dataflow.example.exceptions;

public class PublishingToPubSub1Exception extends RuntimeException {
    public PublishingToPubSub1Exception(String message, Throwable throwable) {
        super(message, throwable);
    }
}
