package com.hasbi.taskManager.controller;

import com.hasbi.taskManager.dto.ProjectDto;
import com.hasbi.taskManager.service.ProjectService;
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

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    void testGetAllProjects() throws Exception {
        ProjectDto project = new ProjectDto();
        project.setId(1L);
        project.setName("Proyecto 1");

        when(projectService.getAllProjects())
                .thenReturn(List.of(project));

        mockMvc.perform(get("/projects/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Proyecto 1"));

        verify(projectService).getAllProjects();
    }

    @Test
    void testGetProjectById() throws Exception {
        ProjectDto project = new ProjectDto();
        project.setId(1L);
        project.setName("Proyecto 1");

        when(projectService.getProjectById(1L))
                .thenReturn(project);

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Proyecto 1"));

        verify(projectService).getProjectById(1L);
    }

    @Test
    void testDeleteProject() throws Exception {
        doNothing().when(projectService).deleteProject(1L);

        mockMvc.perform(delete("/projects/delete/1"))
                .andExpect(status().isNoContent());

        verify(projectService).deleteProject(1L);
    }
}