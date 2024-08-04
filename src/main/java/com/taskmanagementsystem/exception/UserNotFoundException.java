package com.taskmanagementsystem.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long id) {
        super("Not found user with id=" + id);
    }

    public UserNotFoundException(String email) {
        super("User " + email + " not found");
    }
}
