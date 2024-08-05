package com.taskmanagementsystem.repository;

import com.taskmanagementsystem.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitle(String title);
    boolean existsById(Long id);
    Page<Task> findAll(Pageable paging);
    Page<Task> findByAuthor_Id(Long id, Pageable paging);
    Page<Task> findByAssignees_Id(Long id, Pageable paging);
}
