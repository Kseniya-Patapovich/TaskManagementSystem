package com.taskmanagementsystem.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(long id) {
        super("Not found task with id = " + id);
    }

    public TaskNotFoundException(String title) {
        super("Not found task with title = " + title);
    }
}
