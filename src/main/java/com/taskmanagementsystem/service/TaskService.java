package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.DeadlineException;
import com.taskmanagementsystem.exception.TaskNotFoundException;
import com.taskmanagementsystem.exception.UnauthorizedTaskAccessException;
import com.taskmanagementsystem.exception.UserIsNotAuthorException;
import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.model.Task;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.dto.TaskCreateDto;
import com.taskmanagementsystem.model.enums.Priority;
import com.taskmanagementsystem.model.enums.Status;
import com.taskmanagementsystem.repository.TaskRepository;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.UserEntityDetails;
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
        Page<Task> page = taskRepository.findAllByAuthorId(authorId, paging);
        return page.getContent();
    }

    public List<Task> tasksByAssignee(long assigneeId, Pageable paging) {
        if (!userRepository.existsById(assigneeId)) {
            throw new UserNotFoundException(assigneeId);
        }
        Page<Task> page = taskRepository.findAllByAssigneeId(assigneeId, paging);
        return page.getContent();
    }

    @Transactional
    public Long createTask(TaskCreateDto taskCreateDto) {
        Task task = new Task();
        UserEntityDetails userDetail = userUtils.getCurrentUser();
        if (!userRepository.existsByEmail(userDetail.getUsername())) {
            throw new UserNotFoundException(userDetail.getUsername());
        }
        if (!userRepository.existsById(taskCreateDto.getAssigneeId())) {
            throw new UserNotFoundException(taskCreateDto.getAssigneeId());
        }
        if (taskCreateDto.getDeadline().isBefore(LocalDate.now())) {
            throw new DeadlineException(taskCreateDto.getDeadline());
        }
        task.setTitle(taskCreateDto.getTitle());
        task.setDescription(taskCreateDto.getDescription());
        task.setDeadline(taskCreateDto.getDeadline());
        task.setPriority(taskCreateDto.getPriority());
        task.setStatus(Status.PENDING);
        task.setAuthor(userService.getUserByEmail(userDetail.getUsername()));
        task.setAssignee(userRepository.findById(taskCreateDto.getAssigneeId()).get());
        taskRepository.save(task);
        return task.getId();
    }

    @Transactional
    public void editAssignee(long taskId, long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(taskId));
        UserEntity assignee = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserEntityDetails userDetail = userUtils.getCurrentUser();
        if (task.getAuthor() != null && task.getAuthor().getId().equals(userDetail.getId())) {
            task.setAssignee(assignee);
            taskRepository.save(task);
        } else {
            throw new UserIsNotAuthorException(userDetail.getUsername());
        }
    }

    @Transactional
    public void editStatus(long id, Status status) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        UserEntityDetails userEntityDetails = userUtils.getCurrentUser();
        if (task.getAssignee().getId().equals(userEntityDetails.getId())) {
            task.setStatus(status);
            taskRepository.save(task);
        } else {
            throw new UnauthorizedTaskAccessException(userEntityDetails.getUsername());
        }
    }

    @Transactional
    public void editPriority(long id, Priority priority) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        UserEntityDetails userEntityDetails = userUtils.getCurrentUser();
        if (task.getAuthor().getId().equals(userEntityDetails.getId())) {
            task.setPriority(priority);
            taskRepository.save(task);
        } else {
            throw new UserIsNotAuthorException(userEntityDetails.getUsername());
        }
    }

    public void deleteTask(long id) {
        Task taskToDelete = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        UserEntityDetails userEntityDetails = userUtils.getCurrentUser();
        if (taskToDelete.getAuthor().getId().equals(userEntityDetails.getId())) {
            taskRepository.deleteById(id);
        } else {
            throw new UserIsNotAuthorException(userEntityDetails.getUsername());
        }
    }
}
