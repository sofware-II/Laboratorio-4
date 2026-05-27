package com.hasbi.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasbi.taskManager.dto.TaskDto;
import com.hasbi.taskManager.entity.Project;
import com.hasbi.taskManager.entity.Task;
import com.hasbi.taskManager.enums.TaskPriority;
import com.hasbi.taskManager.enums.TaskStatus;
import com.hasbi.taskManager.repository.ProjectRepository;
import com.hasbi.taskManager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests para PUT /tasks/update/{id}
 * 
 * Tests funcionales del endpoint REST de actualización de tareas
 * Casos de prueba con MockMvc y Spring Boot Test Context
 * 
 * @author Task Manager Integration Test Suite
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("Task Update API Integration Tests")
public class TaskUpdateControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private Project testProject;
    private Task testTask;

    // ========== SetUp ==========
    @BeforeEach
    @Transactional
    void setUp() {
        // Crear proyecto de prueba
        testProject = new Project();
        testProject.setName("Integration Test Project");
        testProject.setDescription("Project for integration tests");
        testProject = projectRepository.save(testProject);

        // Crear tarea de prueba
        testTask = new Task();
        testTask.setTitle("Original Task");
        testTask.setDescription("Original Description");
        testTask.setStatus(TaskStatus.TODO);
        testTask.setPriority(TaskPriority.MEDIUM);
        testTask.setProject(testProject);
        testTask = taskRepository.save(testTask);
    }

    // ========== TC01: Actualizar Título vía API ==========
    @Test
    @DisplayName("TC-API-01: PUT /tasks/update/{id} - Actualizar título retorna HTTP 200")
    @Transactional
    void testUpdateTaskTitle_ReturnsOk() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Updated Title", null, null, null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Original Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    // ========== TC02: Actualizar Descripción vía API ==========
    @Test
    @DisplayName("TC-API-02: PUT /tasks/update/{id} - Actualizar descripción retorna datos correctos")
    @Transactional
    void testUpdateTaskDescription_ReturnsOk() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, "Updated Description", null, null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.title").value("Original Task"));
    }

    // ========== TC03: Actualizar Estado vía API ==========
    @Test
    @DisplayName("TC-API-03: PUT /tasks/update/{id} - Actualizar estado a IN_PROGRESS retorna HTTP 200")
    @Transactional
    void testUpdateTaskStatus_ReturnsOk() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, "IN_PROGRESS", null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    // ========== TC04: Actualizar Prioridad vía API ==========
    @Test
    @DisplayName("TC-API-04: PUT /tasks/update/{id} - Actualizar prioridad a HIGH retorna HTTP 200")
    @Transactional
    void testUpdateTaskPriority_ReturnsOk() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, "HIGH", null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    // ========== TC05: Actualizar Múltiples Campos vía API ==========
    @Test
    @DisplayName("TC-API-05: PUT /tasks/update/{id} - Actualizar múltiples campos")
    @Transactional
    void testUpdateTaskMultipleFields_ReturnsOk() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Full Update", "Complete Description", "DONE", "LOW", null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Full Update"))
                .andExpect(jsonPath("$.description").value("Complete Description"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.priority").value("LOW"));
    }

    // ========== TC06: Tarea No Encontrada - HTTP 404 ==========
    @Test
    @DisplayName("TC-API-06: PUT /tasks/update/{id} - Tarea no encontrada retorna HTTP 404")
    void testUpdateTask_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Updated Title", null, null, null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/99999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNotFound());
    }

    // ========== TC07: Estado Inválido - HTTP 400 ==========
    @Test
    @DisplayName("TC-API-07: PUT /tasks/update/{id} - Estado inválido retorna HTTP 400")
    void testUpdateTask_InvalidStatus_ReturnsBadRequest() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, "INVALID_STATUS", null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    // ========== TC08: Prioridad Inválida - HTTP 400 ==========
    @Test
    @DisplayName("TC-API-08: PUT /tasks/update/{id} - Prioridad inválida retorna HTTP 400")
    void testUpdateTask_InvalidPriority_ReturnsBadRequest() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, "INVALID_PRIORITY", null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    // ========== TC09: Campos Nulos Preservan Originales ==========
    @Test
    @DisplayName("TC-API-09: PUT /tasks/update/{id} - Campos nulos preservan valores originales")
    @Transactional
    void testUpdateTask_NullFields_PreservesOriginals() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Original Task"))
                .andExpect(jsonPath("$.description").value("Original Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    // ========== TC10: Respuesta Contiene ID ==========
    @Test
    @DisplayName("TC-API-10: PUT /tasks/update/{id} - Respuesta JSON contiene el ID de la tarea")
    @Transactional
    void testUpdateTask_ResponseContainsId() throws Exception {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Test Update", null, null, null, null);

        // Act & Assert
        mockMvc.perform(put("/tasks/update/" + testTask.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testTask.getId()));
    }

    // ========== TearDown ==========
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Limpiar datos de prueba
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }
}
