# Task Manager Application

## 📝 Overview

A full-stack CRUD application to manage projects and their tasks, built with **Java Spring Boot** (backend) and **React.js** (frontend).

- ✅ RESTful API using Spring Boot
- 🗃️ PostgreSQL with Flyway migrations
- 🧪 JUnit-based unit tests
- ⚛️ Responsive React frontend
- 🎯 Clean architecture and best practices

---

## 🚀 Features

- CRUD operations for **Projects**
- CRUD operations for **Tasks per Project**
- Real-time updates in UI
- Modal confirmation for deletion
- Input validation & error handling
- Separate pages for Projects and Tasks (React Router)

---

## 🛠️ Tech Stack

| Layer     | Technologies                             |
|-----------|------------------------------------------|
| Backend   | Java 21, Spring Boot 3, Hibernate, JPA   |
| Frontend  | React.js (hooks), Axios, React Router    |
| Database  | PostgreSQL                               |
| Migrations| Flyway                                   |
| Testing   | JUnit 5, Mockito                         |
| Build     | Maven, npm/yarn                          |

---

## 📦 Getting Started

### ✅ Prerequisites

- Java 21+
- Maven
- Node.js + npm (or yarn)
- PostgreSQL

---

### 🔧 Backend Setup

```bash
git clone https://github.com/yourusername/task-manager-springboot-reactjs.git
cd task-manager-springboot-reactjs/backend-springboot


🛠️ Configure application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_user
spring.datasource.password=your_password

▶️ Run the backend
mvn clean install
mvn spring-boot:run


Backend will be available at:
👉 http://localhost:8080

✅ Run tests
mvn test
```
---

### 🔍 API Documentation (Swagger)
This project uses SpringDoc OpenAPI to generate interactive API documentation via Swagger UI.

After running the backend, you can access the Swagger interface at:
```
http://localhost:8080/swagger-ui.html
```
---

### 💻 Frontend Setup

```bash
cd ../frontend-reactjs
npm install
npm start

Frontend will be running at:
👉 http://localhost:3000

Ensure backend is running before starting frontend.
```
---

### 🗂️ Project Structure
```
/task-manager
│
├── backend-springboot/
│   ├── src/main/java
│   ├── src/main/resources/
│   │   └── db/migration/V1__init_schema.sql
│   ├── src/test/java/
│   └── pom.xml
│
├── frontend-reactjs/
│   ├── src/components/
│   ├── src/pages/
│   ├── src/api.js
│   └── package.json
```
---

### 📡 API Endpoints

| Action               | Endpoint                     | Method |
| -------------------- | ---------------------------- | ------ |
| Get all projects     | `/projects/all`              | GET    |
| Create project       | `/projects/add`              | POST   |
| Update project       | `/projects/update/{id}`      | PUT    |
| Delete project       | `/projects/delete/{id}`      | DELETE |
| Get tasks by project | `/tasks/project/{projectId}` | GET    |
| Create task          | `/tasks/add`                 | POST   |
| Update task          | `/tasks/update/{id}`         | PUT    |
| Delete task          | `/tasks/delete/{id}`         | DELETE |

---

### 🧪 Testing

All backend services are tested using JUnit 5 and Mockito.
To run tests:
```
cd backend-springboot
mvn test
```
---

📌 Notes

- CORS enabled for frontend-backend communication.
- Logging is enabled using SLF4J.
- Flyway runs automatically on app startup.

---

### 🔐 How to Add Authentication

To add authentication using JWT (JSON Web Tokens):

1. Add Spring Security to the backend
   Add the dependency in pom.xml:
   ```
   <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
   </dependency>
   ```
2. Create User Entity and Auth Controller
   - Define a User entity with roles.
   - Create endpoints for /register and /login.

3. JWT Token Generator & Filter
   - Implement a JwtTokenProvider to issue tokens.
   - Add a JwtAuthenticationFilter to validate tokens in incoming requests.

4. Secure Endpoints
   - Use @PreAuthorize and configure SecurityFilterChain to restrict access.

5. Frontend
   - Save the JWT token in local storage.
   - Use Axios interceptors to attach the token to requests.

---

### 🐳 Docker Setup Instructions
To containerize both backend and frontend apps:

1. Dockerfile for Backend
```
Dockerfile

# backend-springboot/Dockerfile
FROM openjdk:21-jdk
COPY target/backend-springboot.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```
2. Dockerfile for Frontend
```
Dockerfile

# frontend-reactjs/Dockerfile
FROM node:20
WORKDIR /app
COPY . .
RUN npm install && npm run build
RUN npm install -g serve
CMD ["serve", "-s", "build"]
```
3. docker-compose.yml
```
yaml

version: '3.8'
services:
  backend:
    build: ./backend-springboot
    ports:
      - "8080:8080"
  frontend:
    build: ./frontend-reactjs
    ports:
      - "3000:3000"
```
➡️ To run everything:
```
bash

docker-compose up --build
```
---
### 🌐 Future Improvements
## ✅ Internationalization with react-i18next
To support multiple languages and improve accessibility, the frontend can be enhanced using react-i18next.
This library offers powerful and flexible internationalization for React applications, with features like language detection, dynamic loading, and seamless integration with hooks and components.

👉 To learn how to set it up, check the official guide here:
https://react.i18next.com/latest/usetranslation-hook

---
### 📽 Demo Video

👉 [Watch the Demo Video on Google Drive](https://drive.google.com/file/d/1QQrmuyf4VwI6vsNJljS5FxeyRALFXzE2/view?usp=sharing)


