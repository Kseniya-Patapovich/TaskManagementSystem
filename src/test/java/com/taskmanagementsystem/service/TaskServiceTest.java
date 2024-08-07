package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UserIsNotAuthorException;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.utils.UserUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserUtils userUtils;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllTaskSuccess() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("title1");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("title2");

        List<Task> tasks = Arrays.asList(task1, task2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        List<Task> result = taskService.getAllTasks(pageable);

        assertEquals(2, result.size());
        assertEquals("title1", result.get(0).getTitle());
        assertEquals("title2", result.get(1).getTitle());
    }

    @Test
    public void testGetTaskByTitleSuccess() {
        Task task = new Task();
        task.setTitle("title");

        when(taskRepository.findByTitle(anyString())).thenReturn(Optional.of(task));

        Task result = taskService.getTaskByTitle("title");
        assertEquals("title", result.getTitle());
    }


    @Test
    public void testGetTaskByTitleNotFound() {
        when(taskRepository.findByTitle(anyString())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskByTitle("Nonexistent Task");
        });
    }

    @Test
    public void testTasksByAuthor() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        List<Task> tasks = Arrays.asList(task1);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findAllByAuthorId(anyLong(), eq(pageable))).thenReturn(taskPage);

        List<Task> result = taskService.tasksByAuthor(1L, pageable);

        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
    }

    @Test
    public void testTasksByAssignee() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");

        List<Task> tasks = Arrays.asList(task1);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findAllByAssigneeId(anyLong(), eq(pageable))).thenReturn(taskPage);

        List<Task> result = taskService.tasksByAssignee(1L, pageable);

        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
    }

    @Test
    public void testEditAssigneeUserIsNotAuthorException() {
        Task task = new Task();
        task.setId(1L);
        UserEntity author = new UserEntity();
        author.setEmail("other@example.com");

        UserEntity assignee = new UserEntity();
        assignee.setId(2L);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("author@example.com");
        when(userUtils.getCurrentUser()).thenReturn(userDetails);
        when(userService.getUserByEmail(anyString())).thenReturn(author);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(assignee));

        assertThrows(UserIsNotAuthorException.class, () -> taskService.editAssignee(1L, 2L));
    }
}
