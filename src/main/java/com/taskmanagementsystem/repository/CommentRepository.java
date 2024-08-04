package com.taskmanagementsystem.repository;

import com.taskmanagementsystem.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByTaskId(Long taskId, Pageable paging);
}
