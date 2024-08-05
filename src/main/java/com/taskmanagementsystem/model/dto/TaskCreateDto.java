package com.taskmanagementsystem.model.dto;

import com.taskmanagementsystem.model.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data for create new task")
public class TaskCreateDto {
    @Size(min = 5, max = 20)
    @Schema(description = "The title must contain from 5 to 20 characters")
    private String title;

    @Size(min = 1)
    @Schema(description = "Description of task")
    private String description;

    @FutureOrPresent
    @Schema(description = "Deadline cannot be in past")
    private LocalDate deadline;

    private Long assigneeId;

    private Priority priority;
}
