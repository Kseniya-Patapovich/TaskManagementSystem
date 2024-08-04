package com.taskmanagementsystem.model;

import com.taskmanagementsystem.model.enums.Priority;
import com.taskmanagementsystem.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private LocalDate createdDate;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private UserEntity assignee;

    @OneToMany(mappedBy = "task")
    private List<Comment> comments;
}
