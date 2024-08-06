package com.taskmanagementsystem.exception;

public class AlreadyAssigneeException extends RuntimeException{
    public AlreadyAssigneeException(long id) {
        super("User with id=" + id + " is already assignee");
    }
}
