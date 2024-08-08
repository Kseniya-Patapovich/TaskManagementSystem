package com.taskmanagementsystem.security;

import com.taskmanagementsystem.model.UserEntity;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class UserEntityDetails extends User {
    private final long id;

    public UserEntityDetails(UserEntity userEntity) {
        super(userEntity.getEmail(), userEntity.getPassword(), Collections.emptyList());
        this.id = userEntity.getId();
    }
}
