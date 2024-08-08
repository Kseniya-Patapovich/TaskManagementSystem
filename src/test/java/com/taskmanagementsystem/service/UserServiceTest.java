package com.taskmanagementsystem.service;

import com.taskmanagementsystem.exception.UserNotFoundException;
import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsersSuccess() {
        UserEntity test1 = new UserEntity();
        test1.setId(1L);
        test1.setEmail("test1@mail.com");

        UserEntity test2 = new UserEntity();
        test2.setId(2L);
        test2.setEmail("test2@mail.com");

        List<UserEntity> users = Arrays.asList(test1, test2);

        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("test1@mail.com", result.get(0).getEmail());
        assertEquals("test2@mail.com", result.get(1).getEmail());
    }

    @Test
    public void testGetUserByEmailSuccess() {
        UserEntity test = new UserEntity();
        test.setEmail("test@mail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(test));

        UserEntity result = userService.getUserByEmail("test@mail.com");

        assertEquals("test@mail.com", result.getEmail());
    }

    @Test
    public void testGetUserByEmailNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@mail.com");
        });
    }
}
