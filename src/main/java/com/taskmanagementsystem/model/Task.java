package com.taskmanagementsystem.model;

import com.taskmanagementsystem.model.enums.Priority;
import com.taskmanagementsystem.model.enums.Status;
import jakarta.persistence.Column;
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
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

    @Min(10)
    @Max(20)
    @Column
    private String title;

    @Min(0)
    @Column
    private String description;

    @Column
    private LocalDate createdDate =LocalDate.now();

    @FutureOrPresent
    @Column
    private LocalDate deadline;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
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
