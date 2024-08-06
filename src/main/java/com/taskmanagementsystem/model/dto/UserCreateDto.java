package com.taskmanagementsystem.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "Data for create new user")
public class UserCreateDto {
    @Length(min = 7, max = 15)
    @Schema(description = "The username must contain from 7 to 15 characters")
    private String username;

    @Email
    @Schema(description = "Email", example = "junior@example.com")
    private String email;

    @Length(min = 8, max = 32)
    @Schema(description = "The password must contain from 8 to 32 characters")
    private String password;
}
