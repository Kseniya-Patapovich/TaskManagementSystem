package com.taskmanagementsystem.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
