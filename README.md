# Task Management API

A simple REST API for managing tasks, built with **Spring Boot**, **Spring Data JPA**, **H2 Database**, and documented using **Swagger/OpenAPI**.  
This project includes validation, error handling, and full integration testing.

---

## Setup & Run Instructions

### Prerequisites
- Java 13+
- Maven 3.9+
- Any IDE (IntelliJ, Eclipse, VS Code)

### 1. Clone the repository
```bash
git clone https://github.com/DavideMalve/TaskManagement.git
```

### 2. Build the project and run tests
```bash
mvn clean verify
```

### 3. Run the application
```bash
mvn spring-boot:run
```

The API starts on:

```http
http://localhost:8080
```

---

## API Documentation (Swagger)

Swagger UI is already enabled and available at:

```http
http://localhost:8080/swagger-ui.html
```

---

## API Overview

### - Create Task
`POST /api/tasks`

**Body example:**

```json
{
  "title": "Finish homework",
  "description": "Math exercises page 24",
  "status": "PENDING"
}
```

### - Get All Tasks
`GET /api/tasks`

**Supports filter:**

```http
/api/tasks?status=PENDING
```

### - Get Task by ID
`GET /api/tasks/{id}`

### - Update Task
`PUT /api/tasks/{id}`

### - Delete Task
`DELETE /api/tasks/{id}`

---

## Tests

The project contains **JUnit 5 integration tests** covering creation, update, deletion, listing, validation errors, and 404 cases.

Run tests:

```bash
mvn test
```

---

## Project Structure

```
src/
 ├── main/java/com/example/task_management
 │    ├── config           # Security and app configuration
 │    ├── dto              # Request/Response DTOs
 │    ├── factory          # DTO ↔ Entity conversion
 │    ├── model            # JPA entities
 │    ├── repository       # Spring Data JPA repositories
 │    ├── rest             # Controllers
 │    └── service          # Business logic
 └── test/java/com/example/task_management
      ├── integration_test 
      └── unit_test        
```

---

## Design Choices
 
- **Validation:** TaskRequest enforces required title and valid status values  
- **Error Handling:** Centralized via GlobalExceptionHandler for 400/404/invalid enums  
- **Database:** H2 in-memory for simplicity and testing  
- **Testing:** Full integration tests using TestRestTemplate and JUnit 5  
- **Assumptions:** Status values limited to PENDING, IN_PROGRESS, COMPLETED; authentication is basic

---
