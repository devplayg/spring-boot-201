package com.devplayg.coffee.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
