package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.dto.UserCreateDto;
import com.taskmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    @PostMapping("/registration")
    public void registration(@RequestBody UserCreateDto userCreateDto) {
        authService.registration(userCreateDto);
    }

    @PostMapping("/login")
    public String login(@RequestBody String email, @RequestBody String password) {
        return authService.login(email, password);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
