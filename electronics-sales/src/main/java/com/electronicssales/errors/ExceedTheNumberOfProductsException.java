package com.electronicssales.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ExceedTheNumberOfProductsException extends Exception {

    private static final long serialVersionUID = 1L;

    public ExceedTheNumberOfProductsException() {
        super();
    }

    public ExceedTheNumberOfProductsException(String message) {
        super(message);
    }
    
}