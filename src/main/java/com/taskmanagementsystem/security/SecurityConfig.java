package com.taskmanagementsystem.security;

import com.taskmanagementsystem.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService customUserDetailService;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE,"/users").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, "/tasks").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT,"/tasks").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/tasks/**").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/tasks/**").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.GET, "/comments").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.POST, "/comments").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/comments").hasRole(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/comments").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tasks").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tasks/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
