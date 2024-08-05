package com.taskmanagementsystem.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @Email
    private String email;

    @Size(min = 8, max = 32)
    @Schema(description = "The password must contain from 8 to 32 characters")
    private String password;
}
