package com.taskmanagementsystem.exception;

public class CustomJwtException extends RuntimeException {
    public CustomJwtException(String exception) {
        super("Jwt exception: " + exception);
    }
}
