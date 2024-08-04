package com.taskmanagementsystem.controller;

import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.dto.UserCreateDto;
import com.taskmanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /*@PostMapping("/registration")
    public void registration(@RequestBody UserCreateDto userCreateDto) {
        userService.registration(userCreateDto);
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @PostMapping("/logout")
    public void logout() {
        userService.logout();
    }*/

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserEntity getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{email}")
    public UserEntity getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @PutMapping("/password/{id}")
    public void updatePassword(@PathVariable Long id, @RequestBody String password) {
        userService.updatePassword(id, password);
    }

    @PutMapping("/username/{id}")
    public void updateUsername(@PathVariable Long id, @RequestBody String username) {
        userService.updateUsername(id, username);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
    }
}
