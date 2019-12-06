package com.electronicssales.errors;

public class InvalidOldPasswordException extends Error {

    private static final long serialVersionUID = 1L;

    public InvalidOldPasswordException() {
        super();
    }

    public InvalidOldPasswordException(String message) {
        super(message);
    }
    
}