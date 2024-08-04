package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public List<Comment> getCommentByTaskId(@RequestParam long taskId, @RequestParam int page, @RequestParam int limit) {
        return commentService.getCommentsByTaskId(taskId, PageRequest.of(page, limit));
    }

    @PostMapping
    public Long addComment(@RequestParam long taskId, @RequestBody String content) {
        return commentService.addComment(taskId, content);
    }

    @PutMapping
    public void changeComment(@RequestParam long id, @RequestBody String content) {
        commentService.changeComment(id, content);
    }

    @DeleteMapping
    public void deleteComment(@RequestParam long id) {
        commentService.deleteComment(id);
    }
}
