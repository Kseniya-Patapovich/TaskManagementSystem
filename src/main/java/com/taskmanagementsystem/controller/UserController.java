package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "API for user management")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users")
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Gets user by email", description = "Returns user by email, if such exist")
    public UserEntity getUserByEmail(
            @PathVariable
            @Valid
            @Email(message = "Invalid format of email")
            @Parameter(description = "User email", required = true) String email) {
        return userService.getUserByEmail(email);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user", description = "Delete user by his ID")
    public void deleteUser(@RequestParam @Parameter(description = "User ID", required = true) Long id) {
        userService.deleteUser(id);
    }
}