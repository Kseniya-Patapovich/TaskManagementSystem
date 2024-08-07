package com.taskmanagementsystem.repository;

import com.taskmanagementsystem.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitle(String title);
    boolean existsById(Long id);
    Page<Task> findAll(Pageable paging);
    Page<Task> findAllByAuthorId(Long id, Pageable paging);
    Page<Task> findAllByAssigneeId(Long id, Pageable paging);
}
