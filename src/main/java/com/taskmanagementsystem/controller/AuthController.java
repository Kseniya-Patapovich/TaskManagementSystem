package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.dto.LoginDto;
import com.taskmanagementsystem.model.dto.UserCreateDto;
import com.taskmanagementsystem.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API for managing user authentication and registration")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "User registration", description = "Creating a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user has been successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content)
    })
    public void registration(
            @RequestBody
            @Valid
            @Parameter(description = "Data for creating a user", required = true, schema = @Schema(implementation = UserCreateDto.class))
            UserCreateDto userCreateDto) {
        authService.registration(userCreateDto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User login", description = "User authorization in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user has successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Invalid email or password", content = @Content)
    })
    public String login(
            @RequestBody
            @Valid
            @Parameter(description = "Data for login", required = true, schema = @Schema(implementation = LoginDto.class))
            LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "User logout", description = "Termination of the user's session in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user has successfully logged out")
    })
    public void logout() {
        authService.logout();
    }
}
