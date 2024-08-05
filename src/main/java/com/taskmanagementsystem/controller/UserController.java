package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully response"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns user by ID, if such exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully response"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public UserEntity getUserById(@PathVariable @Parameter(description = "User ID", required = true) Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{email}")
    @Operation(summary = "Gets user by email", description = "Returns user by email, if such exist")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully response"),
            @ApiResponse(responseCode = "400", description = "Invalid format of email", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public UserEntity getUserByEmail(
            @PathVariable
            @Valid
            @Email(message = "Invalid format of email")
            @Parameter(description = "User email", required = true) String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/password/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update user password", description = "Updates user password by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "The password has been successfully updated"),
            @ApiResponse(responseCode = "400", description = "The password does not meet the requirements", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)

    })
    public void updatePassword(
            @PathVariable Long id,
            @RequestBody
            @Valid
            @Parameter(description = "New user password", required = true) String password) {
        userService.updatePassword(id, password);
    }

    @PutMapping("/username/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update username", description = "Updates username by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Username has been successfully updated"),
            @ApiResponse(responseCode = "400", description = "Username does not meet the requirements", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public void updateUsername(
            @PathVariable Long id,
            @RequestBody
            @Valid
            @Parameter(description = "New username", required = true) String username) {
        userService.updateUsername(id, username);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user", description = "Delete user by his ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User has been successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public void deleteUser(@RequestParam @Parameter(description = "User ID", required = true) Long id) {
        userService.deleteUser(id);
    }
}
