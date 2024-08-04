package com.taskmanagementsystem.model.dto;

import com.taskmanagementsystem.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UserCreateDto {
    @Min(10)
    @Max(20)
    private String username;

    @Email
    private String email;

    @Min(7)
    @Max(15)
    private String password;
    //private Role role;
}
