package com.taskmanagementsystem.exception;

public class SameUserInDb extends RuntimeException {
    public SameUserInDb(String email) {
        super("User " + email + " already exist");
    }
}
