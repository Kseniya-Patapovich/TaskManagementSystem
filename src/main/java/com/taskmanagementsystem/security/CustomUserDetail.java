package com.taskmanagementsystem.security;

import com.taskmanagementsystem.model.UserEntity;
import com.taskmanagementsystem.model.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetail extends User {
    private final long id;
    private final Role role;

    public CustomUserDetail(UserEntity user) {
        super(user.getEmail(), user.getPassword(), getAuthorities(user.getRole()));
        this.id = user.getId();
        this.role = user.getRole();
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Role role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }
}
