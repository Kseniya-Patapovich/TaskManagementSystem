package com.taskmanagementsystem.service;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserUtils userUtils;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;

    private UserEntity user;
    private UserEntityDetails userEntityDetails;
    private Task task;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("testPassword");

        userEntityDetails = new UserEntityDetails(user);

        task = new Task();
        task.setId(1L);
    }

    @Test
    public void testAddCommentSuccess(){
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userUtils.getCurrentUser()).thenReturn(userEntityDetails);
        when(userService.getUserByEmail(anyString())).thenReturn(user);

        Long commentId = commentService.addComment(1L, "Test Comment");

        ArgumentCaptor<Comment> commentCaptor = ArgumentCaptor.forClass(Comment.class);
        verify(commentRepository).save(commentCaptor.capture());

        Comment savedComment = commentCaptor.getValue();

        assertNotNull(savedComment.getCreatedDate());
        assertEquals(user, savedComment.getAuthor());
        assertEquals(task, savedComment.getTask());
        assertEquals("Test Comment", savedComment.getContent());
    }

    @Test
    public void testGetCommentByTaskIdSuccess(){
        Page<Comment> commentPage = new PageImpl<>(Collections.singletonList(new Comment()));
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        when(commentRepository.findAllByTaskId(anyLong(), any(PageRequest.class))).thenReturn(commentPage);

        assertEquals(1, commentService.getCommentsByTaskId(1L, PageRequest.of(0, 10)).size());
    }

    @Test
    public void testEditCommentSuccess(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userUtils.getCurrentUser()).thenReturn(userEntityDetails);

        commentService.editComment(1L, "Updated Comment");

        assertEquals("Updated Comment", comment.getContent());
        verify(commentRepository).save(comment);
    }

    @Test
    public void testEditCommentUserIsNotAuthorException(){
        Comment comment = new Comment();
        comment.setId(1L);
        UserEntity differentUser = new UserEntity();
        differentUser.setId(2L);
        comment.setAuthor(differentUser);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userUtils.getCurrentUser()).thenReturn(userEntityDetails);

        assertThrows(UserIsNotAuthorException.class, () -> commentService.editComment(1L, "Updated Comment"));
    }

    @Test
    public void testDeleteCommentSuccess(){
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userUtils.getCurrentUser()).thenReturn(userEntityDetails);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    public void testDeleteCommentUserIsNotAuthorException(){
        Comment comment = new Comment();
        comment.setId(1L);
        UserEntity differentUser = new UserEntity();
        differentUser.setId(2L);
        comment.setAuthor(differentUser);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(userUtils.getCurrentUser()).thenReturn(userEntityDetails);

        assertThrows(UserIsNotAuthorException.class, () -> commentService.deleteComment(1L));
    }
}
