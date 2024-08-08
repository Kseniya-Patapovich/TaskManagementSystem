package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UserIsNotAuthorException;
import com.taskmanagementsystem.model.Comment;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.CommentRepository;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.security.UserEntityDetails;
import com.taskmanagementsystem.utils.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserUtils userUtils;
    @InjectMocks
    private CommentService commentService;

    private UserEntity author;
    private Task task;
    private Comment comment;

    @BeforeEach
    void setUp() {
        author = new UserEntity();
        author.setId(1L);
        author.setEmail("author@example.com");

        task = new Task();
        task.setId(1L);

        comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(author);
        comment.setTask(task);
        comment.setContent("Original content");
    }

    @Test
    void testGetCommentsByTaskId_Success() {
        Pageable paging = Pageable.ofSize(10);
        Page<Comment> page = new PageImpl<>(Collections.singletonList(comment));
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(commentRepository.findAllByTaskId(1L, paging)).thenReturn(page);

        assertEquals(1, commentService.getCommentsByTaskId(1L, paging).size());
    }

    @Test
    void testGetCommentsByTaskId_TaskNotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> commentService.getCommentsByTaskId(1L, Pageable.ofSize(10)));
    }

    @Test
    void testEditComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        UserEntityDetails userDetails = mock(UserEntityDetails.class);
        when(userDetails.getUsername()).thenReturn(author.getEmail());
        when(userUtils.getCurrentUser()).thenReturn(userDetails);
        when(userService.getUserByEmail(author.getEmail())).thenReturn(author);

        commentService.editComment(1L, "Updated content");
        assertEquals("Updated content", comment.getContent());
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void testEditComment_UserIsNotAuthor() {
        UserEntity otherUser = new UserEntity();
        otherUser.setEmail("other@example.com");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        UserEntityDetails userDetails = mock(UserEntityDetails.class);
        when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
        when(userUtils.getCurrentUser()).thenReturn(userDetails);
        when(userService.getUserByEmail(otherUser.getEmail())).thenReturn(otherUser);

        assertThrows(UserIsNotAuthorException.class, () -> commentService.editComment(1L, "Updated content"));
    }

    @Test
    void testDeleteComment_Success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        UserEntityDetails userDetails = mock(UserEntityDetails.class);
        when(userDetails.getUsername()).thenReturn(author.getEmail());
        when(userUtils.getCurrentUser()).thenReturn(userDetails);
        when(userService.getUserByEmail(author.getEmail())).thenReturn(author);

        commentService.deleteComment(1L);
        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteComment_UserIsNotAuthor() {
        UserEntity otherUser = new UserEntity();
        otherUser.setEmail("other@example.com");

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        UserEntityDetails userDetails = mock(UserEntityDetails.class);
        when(userDetails.getUsername()).thenReturn(otherUser.getEmail());
        when(userUtils.getCurrentUser()).thenReturn(userDetails);
        when(userService.getUserByEmail(otherUser.getEmail())).thenReturn(otherUser);

        assertThrows(UserIsNotAuthorException.class, () -> commentService.deleteComment(1L));
    }
}
