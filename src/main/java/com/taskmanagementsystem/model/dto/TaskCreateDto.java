package com.taskmanagementsystem.model.dto;

import com.taskmanagementsystem.model.enums.Priority;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateDto {
    private String title;
    private String description;
    private LocalDate deadline;
    private Long assignee_id;
    private Priority priority;
}
