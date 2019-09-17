package com.electronicssales.exceptions;

import com.electronicssales.entities.User;

public class UserExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Field field;

    private String message;

    public static enum Field {
        EMAIL,

        PHONE_NUMBER,

        USERNAME;
    }

    public UserExistsException(Field field) {
        super();
        this.field = field;
        this.message = buildMessage();
    }

    private String buildMessage() {
        StringBuilder builder = new StringBuilder();
        builder
            .append(User.class.getSimpleName().toUpperCase())
            .append(" with ")
            .append(field.toString())
            .append(" is already exists !");
        return builder.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

}