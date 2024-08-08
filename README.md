# Task Management System

## Description
Task Management System is a RESTful API application designed for task management. Users can create, edit, delete tasks, assign performers, and manage task statuses. The system supports user registration and authentication.

## Installation and launch
1. ```git clone https://github.com/Kseniya-Patapovich/TaskManagementSystem.git```
2. ```docker-compose build```
3. ```docker-compose up```

## Technologies
- Java 17
- Spring Boot
- Spring Security
- Postgres
- Flyway
- Lombok
- JWT
- JUnit & Mockito
- Swagger

## API Documentation
API documentation is available through the Swagger UI. To access it, open a browser and go to the following address: ```http://localhost:8080/swagger-ui/index.html```

## Features
- User registration: Create a new user in the system.

  Example:

  ```POST /auth/registration```

  ```json
  {
  "username": "newUser",
  "email": "newuser@mail.com",
  "password": "newUserPassword"
  }
  ```
- Authentication: Login using JWT tokens.

  Example:

  ```POST /auth/login```

  ```json
  {
  "email": "newuser@example.com",
  "password": "password"
  }
  ```

- Task Management:
  - Creating new tasks.
  - Assigning tasks to users.
  - Changing the status of tasks (for example, IN_PROGRESS, COMPLETED).
  - Changing the priority of tasks (for example, HIGH, MEDIUM, LOW).
  - Deleting tasks.
- Viewing tasks:
  - View all tasks.
  - View tasks created by a specific author.
  - View the tasks assigned to a specific performer.
- Task Comments:
  - Users can leave comments on tasks.
  - View all comments related to the task.
  - Deleting comments.
  -  Editing comments.
