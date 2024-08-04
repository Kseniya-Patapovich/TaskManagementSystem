package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.CommentNotFoundException;
import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.CommentRepository;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.CustomUserDetail;
import com.taskmanagementsystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserUtils userUtils;

    @Transactional
    public Long addComment(long taskId, String content) {
        Comment comment = new Comment();
        CustomUserDetail currentUserDetail = userUtils.getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        UserEntity author = userRepository.findById(currentUserDetail.getId()).orElseThrow(() -> new UserNotFoundException(currentUserDetail.getId()));
        comment.setCreatedDate(LocalDate.now());
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setText(content);
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
    public void changeComment(long id, String content) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        comment.setText(content);
        commentRepository.save(comment);
    }

    public void deleteComment(long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }
        commentRepository.deleteById(id);
    }
}
