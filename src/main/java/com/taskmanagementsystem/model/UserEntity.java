package com.taskmanagementsystem.model;

import com.taskmanagementsystem.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(10)
    @Max(20)
    private String username;

    @Column(unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    @Min(7)
    @Max(15)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "author")
    private List<Task> createdTask;

    @OneToMany(mappedBy = "assignee")
    private List<Task> assignedTask;

    @OneToMany(mappedBy = "author")
    private List<Comment> comments;
}
