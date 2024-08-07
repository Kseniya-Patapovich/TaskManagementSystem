package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "API for managing comments on tasks")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get comments by task ID", description = "Returns a paginated list of comments for a specified task.")
    public List<Comment> getCommentByTaskId(
            @RequestParam @Parameter(description = "Task ID", required = true) long taskId,
            @RequestParam @Parameter(description = "Page number", example = "0", required = true) int page,
            @RequestParam @Parameter(description = "Number of comments per page", example = "10", required = true) int limit) {
        return commentService.getCommentsByTaskId(taskId, PageRequest.of(page, limit));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a comment to a task", description = "Adds a new comment to the specified task.")
    public Long addComment(
            @RequestParam @Parameter(description = "Task ID", required = true) long taskId,
            @RequestBody @Parameter(description = "Content of the comment", required = true) String content) {
        return commentService.addComment(taskId, content);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update a comment", description = "Updates the content of an existing comment.")
    public void editComment(
            @RequestParam @Parameter(description = "Comment ID", required = true) long id,
            @RequestBody @Parameter(description = "Updated content of the comment", required = true) String content) {
        commentService.editComment(id, content);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a comment", description = "Deletes a comment by its ID.")
    public void deleteComment(@RequestParam @Parameter(description = "Comment ID", required = true) long id) {
        commentService.deleteComment(id);
    }
}