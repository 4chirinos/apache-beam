package com.dataflow.example.exceptions;

public class PublishingToPubSub2Exception extends RuntimeException {
    public PublishingToPubSub2Exception(String message, Throwable throwable) {
        super(message, throwable);
    }
}
