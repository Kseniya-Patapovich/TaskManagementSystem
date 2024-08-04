package com.taskmanagementsystem.exception;

import java.time.LocalDate;

public class WarningDeadlineException extends RuntimeException {
    public WarningDeadlineException(LocalDate date) {
        super("Date " + date + " cannot be in past");
    }
}
