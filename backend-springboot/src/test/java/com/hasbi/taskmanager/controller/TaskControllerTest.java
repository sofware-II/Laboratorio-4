package com.hasbi.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasbi.taskmanager.dto.TaskDto;
import com.hasbi.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTask() throws Exception {
        // Arrange
        TaskDto request = new TaskDto(
                null,
                "Nueva tarea",
                "Descripción nueva",
                "TODO",
                "MEDIUM",
                10L
        );

        TaskDto response = new TaskDto(
                1L,
                "Nueva tarea",
                "Descripción nueva",
                "TODO",
                "MEDIUM",
                10L
        );

        when(taskService.createTask(Mockito.any(TaskDto.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(post("/tasks/add")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Nueva tarea"))
                .andExpect(jsonPath("$.description").value("Descripción nueva"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.projectId").value(10L));

        verify(taskService).createTask(Mockito.any(TaskDto.class));
    }

    @Test
    void shouldReturnTasksByProjectId() throws Exception {
        // Arrange
        TaskDto taskDto = new TaskDto(
                1L,
                "Test Task",
                "Test Desc",
                "TODO",
                "MEDIUM",
                10L
        );

        when(taskService.getTasksByProjectId(10L)).thenReturn(List.of(taskDto));

        // Act + Assert
        mockMvc.perform(get("/tasks/project/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].description").value("Test Desc"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"))
                .andExpect(jsonPath("$[0].projectId").value(10L));

        verify(taskService).getTasksByProjectId(10L);
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        // Arrange
        TaskDto taskDto = new TaskDto(
                1L,
                "Test Task",
                "Test Desc",
                "TODO",
                "MEDIUM",
                10L
        );

        when(taskService.getTaskById(1L)).thenReturn(taskDto);

        // Act + Assert
        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Desc"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.projectId").value(10L));

        verify(taskService).getTaskById(1L);
    }

    @Test
    void shouldUpdateTask() throws Exception {
        // Arrange
        TaskDto request = new TaskDto(
                null,
                "Tarea actualizada",
                "Descripción actualizada",
                "IN_PROGRESS",
                "HIGH",
                10L
        );

        TaskDto response = new TaskDto(
                1L,
                "Tarea actualizada",
                "Descripción actualizada",
                "IN_PROGRESS",
                "HIGH",
                10L
        );

        when(taskService.updateTask(Mockito.eq(1L), Mockito.any(TaskDto.class))).thenReturn(response);

        // Act + Assert
        mockMvc.perform(put("/tasks/update/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Tarea actualizada"))
                .andExpect(jsonPath("$.description").value("Descripción actualizada"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.projectId").value(10L));

        verify(taskService).updateTask(Mockito.eq(1L), Mockito.any(TaskDto.class));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        // Act + Assert
        mockMvc.perform(delete("/tasks/delete/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }
}