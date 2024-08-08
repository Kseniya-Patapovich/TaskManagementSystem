package com.taskmanagementsystem.utils;

import com.taskmanagementsystem.security.UserEntityDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public UserEntityDetails getCurrentUser() {
        return (UserEntityDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
