package com.taskmanagementsystem.exception;

public class UserIsNotAuthorException extends RuntimeException{
    public UserIsNotAuthorException(String email) {
        super("User " + email + " isn't author!");
    }
}
