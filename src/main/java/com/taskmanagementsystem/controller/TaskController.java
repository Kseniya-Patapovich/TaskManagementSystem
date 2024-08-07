package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.dto.TaskCreateDto;
import com.taskmanagementsystem.model.enums.Priority;
import com.taskmanagementsystem.model.enums.Status;
import com.taskmanagementsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "API for managing tasks")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all tasks", description = "Returns a paginated list of all tasks.")
    public List<Task> getAllTasks(
            @RequestParam @Parameter(description = "Page number", example = "0", required = true) int page,
            @RequestParam @Parameter(description = "Number of tasks per page", example = "10", required = true) int limit) {
        return taskService.getAllTasks(PageRequest.of(page, limit));
    }

    @GetMapping("/{title}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get task by title", description = "Returns a task by its title.")
    public Task getTaskByTitle(@PathVariable @Parameter(description = "Task title", required = true) String title) {
        return taskService.getTaskByTitle(title);
    }

    @GetMapping("/by_author")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tasks by author", description = "Returns a paginated list of tasks created by the specified author.")
    public List<Task> getTasksByAuthor(
            @RequestParam @Parameter(description = "Author ID", required = true) long authorId,
            @RequestParam @Parameter(description = "Page number", example = "0", required = true) int page,
            @RequestParam @Parameter(description = "Number of tasks per page", example = "10", required = true) int limit) {
        return taskService.tasksByAuthor(authorId, PageRequest.of(page, limit));
    }

    @GetMapping("/by_assignee")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get tasks by assignee", description = "Returns a paginated list of tasks assigned to the specified user.")
    public List<Task> getTasksByAssignee(
            @RequestParam @Parameter(description = "Assignee ID", required = true) long assigneeId,
            @RequestParam @Parameter(description = "Page number", example = "0", required = true) int page,
            @RequestParam @Parameter(description = "Number of tasks per page", example = "10", required = true) int limit) {
        return taskService.tasksByAssignee(assigneeId, PageRequest.of(page, limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a task", description = "Creates a new task.")
    public Long createTask(@RequestBody @Valid @Parameter(description = "Task creation details", required = true) TaskCreateDto taskCreateDto) {
        return taskService.createTask(taskCreateDto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Assign a user to a task", description = "Assigns a user to an existing task.")
    public void editAssignee(
            @RequestParam @Parameter(description = "Task ID", required = true) long taskId,
            @RequestParam @Parameter(description = "Assignee ID", required = true) long assigneeId) {
        taskService.editAssignee(taskId, assigneeId);
    }

    @PutMapping("/status/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change task status", description = "Changes the status of a task.")
    public void editStatus(
            @PathVariable @Parameter(description = "Task ID", required = true) long id,
            @RequestBody @Parameter(description = "New status", required = true) Status status) {
        taskService.editStatus(id, status);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task", description = "Deletes a task by its ID.")
    public void deleteTask(@PathVariable @Parameter(description = "Task ID", required = true) long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/priority/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update task's priority", description = "Update task's priority by its id")
    public void editPriority(
            @PathVariable @Parameter(description = "task ID", required = true) long id,
            @RequestBody @Parameter(description = "Priority") Priority priority){
        taskService.editPriority(id, priority);
    }
}
