package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UnauthorizedTaskAccessException;
import com.taskmanagementsystem.exception.UserIsNotAuthorException;
import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.exception.WarningDeadlineException;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.dto.TaskCreateDto;
import com.taskmanagementsystem.model.enums.Status;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.CustomUserDetail;
import com.taskmanagementsystem.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserUtils userUtils;
    private final UserRepository userRepository;

    public List<Task> getAllTasks(Pageable paging) {
        Page<Task> page = taskRepository.findAll(paging);
        return page.getContent();
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task getTaskByTitle(String title) {
        return taskRepository.findByTitle(title).orElseThrow(() -> new TaskNotFoundException(title));
    }

    public List<Task> tasksByAuthor(long authorId, Pageable paging) {
        if (!userRepository.existsById(authorId)) {
            throw new UserNotFoundException(authorId);
        }
        Page<Task> page = taskRepository.findByAuthor_Id(authorId, paging);
        return page.getContent();
    }

    public List<Task> tasksByAssignee(long assigneeId, Pageable paging) {
        if (!userRepository.existsById(assigneeId)) {
            throw new UserNotFoundException(assigneeId);
        }
        Page<Task> page = taskRepository.findByAssignee_Id(assigneeId, paging);
        return page.getContent();
    }

    @Transactional
    public Long createTask(TaskCreateDto taskCreateDto) {
        Task task = new Task();
        CustomUserDetail userDetail = userUtils.getCurrentUser();
        UserEntity author = userRepository.findById(userDetail.getId()).orElseThrow(() -> new UserNotFoundException(userDetail.getId()));
        UserEntity assignee = userRepository.findById(taskCreateDto.getAssignee_id()).orElseThrow(() -> new UserNotFoundException(taskCreateDto.getAssignee_id()));
        if (taskCreateDto.getDeadline().isBefore(LocalDate.now())){
            throw new WarningDeadlineException(taskCreateDto.getDeadline());
        }
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setCreatedDate(LocalDate.now());
        task.setDeadline(taskCreateDto.getDeadline());
        task.setPriority(taskCreateDto.getPriority());
        task.setStatus(Status.PENDING);
        task.setAuthor(author);
        task.setAssignee(assignee);
        taskRepository.save(task);
        return task.getId();
    }

    @Transactional
    public void assignTaskToUser(long taskId, long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (task.getAuthor() != null && task.getAuthor().equals(userRepository.findByEmail(currentUserEmail))) {
            task.setAssignee(user);
            taskRepository.save(task);
        } else {
            throw new UserIsNotAuthorException(currentUserEmail);
        }
    }

    @Transactional
    public void changeStatus(long id, Status status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(currentUserEmail).orElseThrow(() -> new UserNotFoundException(currentUserEmail));
        if (task.getAssignee() != null && task.getAssignee().equals(currentUser)) {
            task.setStatus(status);
            taskRepository.save(task);
        } else {
            throw new UnauthorizedTaskAccessException(currentUserEmail);
        }
    }

    public void deleteTask(long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else {
            throw new TaskNotFoundException(id);
        }
    }
}
