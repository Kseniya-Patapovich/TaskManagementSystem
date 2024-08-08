package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.CommentNotFoundException;
import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UserIsNotAuthorException;
import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.CommentRepository;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.security.UserEntityDetails;
import com.taskmanagementsystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserUtils userUtils;
    private final UserService userService;

    @Transactional
    public Long addComment(long taskId, String content) {
        Comment comment = new Comment();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        UserDetails currentUserDetail = userUtils.getCurrentUser();
        UserEntity author = userService.getUserByEmail(currentUserDetail.getUsername());
        comment.setCreatedDate(LocalDate.now());
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setContent(content);
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<Comment> getCommentsByTaskId(long taskId, Pageable paging) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(taskId);
        }
        Page<Comment> page = commentRepository.findAllByTaskId(taskId, paging);
        return page.getContent();
    }

    @Transactional
    public void editComment(long id, String content) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        UserEntityDetails userEntityDetails = userUtils.getCurrentUser();
        if (comment.getAuthor().getId().equals(userEntityDetails.getId())) {
            comment.setContent(content);
            commentRepository.save(comment);
        } else {
            throw new UserIsNotAuthorException(userEntityDetails.getUsername());
        }
    }

    public void deleteComment(long id) {
        Comment commentToDelete = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        UserEntityDetails userEntityDetails = userUtils.getCurrentUser();
        if (commentToDelete.getAuthor().getId().equals(userEntityDetails.getId())) {
            commentRepository.deleteById(id);
        } else {
            throw new UserIsNotAuthorException(userEntityDetails.getUsername());
        }
    }
}
