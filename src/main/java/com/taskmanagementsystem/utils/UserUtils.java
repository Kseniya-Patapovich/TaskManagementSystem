package com.taskmanagementsystem.utils;

import com.taskmanagementsystem.security.CustomUserDetail;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public CustomUserDetail getCurrentUser() {
        return (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
