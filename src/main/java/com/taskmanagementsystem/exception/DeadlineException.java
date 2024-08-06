package com.taskmanagementsystem.exception;

import java.time.LocalDate;

public class DeadlineException extends RuntimeException {
    public DeadlineException(LocalDate date) {
        super("Date " + date + " cannot be in past");
    }
}
