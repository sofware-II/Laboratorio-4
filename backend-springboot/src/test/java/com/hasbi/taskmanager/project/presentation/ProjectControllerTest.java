package com.hasbi.taskmanager.project.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasbi.taskmanager.project.application.dto.ProjectDto;
import com.hasbi.taskmanager.project.application.service.ProjectService;
import com.hasbi.taskmanager.controller.ApiPaths;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProjects() throws Exception {
        // Arrange
        ProjectDto project = new ProjectDto(1L, "Proyecto 1", "Descripción 1");

        when(projectService.getAllProjects()).thenReturn(List.of(project));

        // Act + Assert
        mockMvc.perform(get(ApiPaths.PROJECTS + ApiPaths.PROJECTS_ALL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Proyecto 1"))
                .andExpect(jsonPath("$[0].description").value("Descripción 1"));

        verify(projectService).getAllProjects();
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        // Arrange
        ProjectDto project = new ProjectDto(1L, "Proyecto 1", "Descripción 1");

        when(projectService.getProjectById(1L)).thenReturn(project);

        // Act + Assert
        mockMvc.perform(get(ApiPaths.PROJECTS + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Proyecto 1"))
                .andExpect(jsonPath("$.description").value("Descripción 1"));

        verify(projectService).getProjectById(1L);
    }

    @Test
    void shouldUpdateProject() throws Exception {
        // Arrange
        ProjectDto request = new ProjectDto(null, "Proyecto actualizado", "Descripción actualizada");
        ProjectDto response = new ProjectDto(1L, "Proyecto actualizado", "Descripción actualizada");

        when(projectService.updateProject(Mockito.eq(1L), Mockito.any(ProjectDto.class)))
                .thenReturn(response);

        // Act + Assert
        mockMvc.perform(put(ApiPaths.PROJECTS + "/update/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Proyecto actualizado"))
                .andExpect(jsonPath("$.description").value("Descripción actualizada"));

        verify(projectService).updateProject(Mockito.eq(1L), Mockito.any(ProjectDto.class));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        // Arrange
        doNothing().when(projectService).deleteProject(1L);

        // Act + Assert
        mockMvc.perform(delete(ApiPaths.PROJECTS + "/delete/1"))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProject(1L);
    }
}
