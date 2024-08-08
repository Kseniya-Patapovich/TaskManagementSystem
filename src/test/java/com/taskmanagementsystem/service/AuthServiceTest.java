package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.SameUserInDb;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.dto.LoginDto;
import com.taskmanagementsystem.model.dto.UserCreateDto;
import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrationSuccess() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("testUsername");
        userCreateDto.setEmail("test@mail.com");
        userCreateDto.setPassword("testPassword");

        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");

        authService.registration(userCreateDto);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testRegistrationUserAlreadyExists() {
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setUsername("testUsername");
        userCreateDto.setEmail("test@mail.com");
        userCreateDto.setPassword("testPassword");

        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.of(new UserEntity()));

        assertThrows(SameUserInDb.class, () -> authService.registration(userCreateDto));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testLoginSuccess() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtUtils.generateJwtToken(any(String.class))).thenReturn("jwtToken");

        String token = authService.login(loginDto);

        assertEquals("jwtToken", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
