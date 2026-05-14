package com.hasbi.taskManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasbi.taskManager.dto.TaskDto;
import com.hasbi.taskManager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTasksByProjectId() throws Exception {
        TaskDto task = new TaskDto();
        task.setId(1L);
        task.setTitle("Tarea 1");

        when(taskService.getTasksByProjectId(10L))
                .thenReturn(List.of(task));

        mockMvc.perform(get("/tasks/project/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Tarea 1"));

        verify(taskService).getTasksByProjectId(10L);
    }

    @Test
    void testGetTaskById() throws Exception {
        TaskDto task = new TaskDto();
        task.setId(1L);
        task.setTitle("Tarea 1");

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Tarea 1"));

        verify(taskService).getTaskById(1L);
    }

    @Test
    void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/tasks/delete/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }
}