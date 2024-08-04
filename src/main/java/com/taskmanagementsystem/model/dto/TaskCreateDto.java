package com.taskmanagementsystem.model.dto;

import com.taskmanagementsystem.model.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateDto {
    @Min(10)
    @Max(20)
    private String title;

    @Min(0)
    private String description;

    @FutureOrPresent
    private LocalDate deadline;

    private Long assigneeId;

    private Priority priority;
}
