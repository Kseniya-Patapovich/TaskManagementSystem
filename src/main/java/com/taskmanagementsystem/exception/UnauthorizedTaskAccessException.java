package com.taskmanagementsystem.exception;

public class UnauthorizedTaskAccessException extends RuntimeException {
    public UnauthorizedTaskAccessException(String email) {
        super("User " + email + "doesn't have the authority to change the status!");
    }
}
