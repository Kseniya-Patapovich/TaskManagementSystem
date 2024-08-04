package com.taskmanagementsystem.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    private String email;

    @Min(7)
    @Max(15)
    private String password;
}
