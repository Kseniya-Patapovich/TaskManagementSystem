package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.SameUserInDb;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.dto.LoginDto;
import com.taskmanagementsystem.model.dto.UserCreateDto;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void registration(UserCreateDto userCreateDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userCreateDto.getEmail());
        if (user.isPresent()) {
            throw new SameUserInDb(userCreateDto.getEmail());
        }
        UserEntity createdUser = new UserEntity();
        createdUser.setUsername(userCreateDto.getUsername());
        createdUser.setEmail(userCreateDto.getEmail());
        createdUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        userRepository.save(createdUser);
    }

    public String login(LoginDto loginDto) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtUtils.generateJwtToken(loginDto.getEmail());
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }
}
