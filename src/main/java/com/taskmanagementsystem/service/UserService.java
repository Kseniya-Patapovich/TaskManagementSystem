package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }
}
