# Student Management REST API

A backend REST API for managing students, teachers, lessons, and academic records. Built with Spring Boot and secured with JWT-based authentication.

## Technologies

- Java 17
- Spring Boot
- Spring Security (JWT Authentication)
- Spring Data JPA / Hibernate
- PostgreSQL
- Maven
- Swagger / OpenAPI

## Features

- Role-based access control (ADMIN, MANAGER, ASSISTANT_MANAGER, TEACHER, STUDENT)
- JWT access token + refresh token authentication
- Student, Teacher and User management
- Lesson and Education Term management
- Student grade and academic info tracking
- Meeting scheduling between advisors and students
- Pagination and sorting on all list endpoints
- Contact message system
- Swagger UI for API documentation

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL
- Maven

### Setup

1. Clone the repository
```bash
   git clone https://github.com/ahmetfrkan/student-management-api.git
```

2. Create a PostgreSQL database

3. Set the following environment variables:

4. Update `src/main/resources/application.properties` with your database name and username

5. Run the application
```bash
   ./mvnw spring-boot:run
```

6. Access Swagger UI at:
http://localhost:8080/swagger-ui/index.html
