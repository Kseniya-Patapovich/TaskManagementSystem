package com.taskmanagementsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Task Management System",
        description = "API for task management",
        contact = @Contact(name = "Kseniya",
                url = "https://github.com/Kseniya-Patapovich",
                email = "ksyu.potapovich@bk.ru")
),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SpringBootApplication
public class TaskManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);
    }

}
