package com.taskmanagementsystem.utils;

import com.taskmanagementsystem.repository.UserRepository;
import com.taskmanagementsystem.security.CustomUserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final UserRepository userRepository;

    public CustomUserDetail getCurrentUser() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
