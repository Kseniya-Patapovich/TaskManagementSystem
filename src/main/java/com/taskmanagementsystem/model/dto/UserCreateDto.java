package com.taskmanagementsystem.model.dto;

import com.taskmanagementsystem.model.enums.Role;
import lombok.Data;

@Data
public class UserCreateDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
