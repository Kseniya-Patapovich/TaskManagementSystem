package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.dto.TaskCreateDto;
import com.taskmanagementsystem.model.enums.Status;
import com.taskmanagementsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<Task> getAllTasks(@RequestParam int page, @RequestParam int limit) {
        return taskService.getAllTasks(PageRequest.of(page, limit));
    }

    @GetMapping("/{title}")
    public Task getTaskByTitle(@PathVariable String title) {
        return taskService.getTaskByTitle(title);
    }

    @GetMapping("/by_author")
    public List<Task> getTaskByAuthor(@RequestParam long authorId, @RequestParam int page, @RequestParam int limit) {
        return taskService.tasksByAuthor(authorId, PageRequest.of(page, limit));
    }

    @GetMapping("/by_assignee")
    public List<Task> getTaskByAssignee(@RequestParam long assigneeId, @RequestParam int page, @RequestParam int limit) {
        return taskService.tasksByAssignee(assigneeId, PageRequest.of(page, limit));
    }

    @PostMapping
    public Long createTask(@RequestBody TaskCreateDto taskCreateDto) {
        return taskService.createTask(taskCreateDto);
    }

    @PutMapping
    public void assignUserToTask(@RequestParam long taskId, @RequestParam long assigneeId) {
        taskService.assignTaskToUser(taskId, assigneeId);
    }

    @PutMapping("/{id}")
    public void changeStatus(@PathVariable long id, @RequestBody Status status) {
        taskService.changeStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }
}
