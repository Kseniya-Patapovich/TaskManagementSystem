package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.AlreadyAssigneeException;
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
    private final UserService userService;

    public List<Task> getAllTasks(Pageable paging) {
        Page<Task> page = taskRepository.findAll(paging);
        return page.getContent();
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
        Page<Task> page = taskRepository.findByAssignees_Id(assigneeId, paging);
        return page.getContent();
    }

    @Transactional
    public Long createTask(TaskCreateDto taskCreateDto) {
        Task task = new Task();
        CustomUserDetail userDetail = userUtils.getCurrentUser();
        if (!userRepository.existsById(userDetail.getId())) {
            throw new UserNotFoundException(userDetail.getId());
        }
        if (!userRepository.existsById(taskCreateDto.getAssigneeId())) {
            throw new UserNotFoundException(taskCreateDto.getAssigneeId());
        }
        if (taskCreateDto.getDeadline().isBefore(LocalDate.now())) {
            throw new WarningDeadlineException(taskCreateDto.getDeadline());
        }
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setDeadline(taskCreateDto.getDeadline());
        task.setPriority(taskCreateDto.getPriority());
        task.setStatus(Status.PENDING);
        task.setAuthor(userService.getUserById(userDetail.getId()));
        task.getAssignees().add(userService.getUserById(taskCreateDto.getAssigneeId()));
        taskRepository.save(task);
        return task.getId();
    }

    @Transactional
    public void assignTaskToUser(long taskId, long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        UserEntity assignee = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        if (task.getAssignees().contains(assignee)) {
            throw new AlreadyAssigneeException(userId);
        }
        CustomUserDetail userDetail = userUtils.getCurrentUser();
        UserEntity author = userService.getUserById(userDetail.getId());
        if (task.getAuthor() != null && task.getAuthor().equals(author)) {
            task.getAssignees().add(assignee);
            taskRepository.save(task);
        } else {
            throw new UserIsNotAuthorException(userDetail.getUsername());
        }
    }

    @Transactional
    public void changeStatus(long id, Status status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByEmail(currentUserEmail).get();
        if (task.getAssignees().contains(currentUser)) {
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
