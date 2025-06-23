package com.hasbi.taskManager.service;

import com.hasbi.taskManager.dto.ProjectDto;
import com.hasbi.taskManager.entity.Project;
import com.hasbi.taskManager.exception.ResourceNotFoundException;
import com.hasbi.taskManager.mapper.ProjectMapper;
import com.hasbi.taskManager.repository.ProjectRepository;
import com.hasbi.taskManager.service.impl.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    private Project project;
    private ProjectDto projectDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        project = new Project(1L, "Test Project", "Test Description");
        projectDto = new ProjectDto(1L, "Test Project", "Test Description");
    }

    @Test
    void createProject_ShouldReturnSavedDto() {
        when(projectMapper.toEntity(projectDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto result = projectService.createProject(projectDto);

        assertNotNull(result);
        assertEquals(projectDto.getName(), result.getName());
        verify(projectRepository).save(project);
    }

    @Test
    void getAllProjects_ShouldReturnListOfDtos() {
        List<Project> projects = Arrays.asList(project);
        when(projectRepository.findAll()).thenReturn(projects);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        List<ProjectDto> result = projectService.getAllProjects();

        assertEquals(1, result.size());
        assertEquals(projectDto.getName(), result.get(0).getName());
    }

    @Test
    void getProjectById_WhenFound_ShouldReturnDto() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto result = projectService.getProjectById(1L);

        assertNotNull(result);
        assertEquals(projectDto.getName(), result.getName());
    }

    @Test
    void getProjectById_WhenNotFound_ShouldThrowException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void updateProject_WhenFound_ShouldUpdateAndReturnDto() {
        ProjectDto updateDto = new ProjectDto(null, "Updated Name", null);
        Project updatedProject = new Project(1L, "Updated Name", "Test Description");
        ProjectDto updatedDto = new ProjectDto(1L, "Updated Name", "Test Description");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);
        when(projectMapper.toDto(updatedProject)).thenReturn(updatedDto);

        ProjectDto result = projectService.updateProject(1L, updateDto);

        assertEquals("Updated Name", result.getName());
        assertEquals(project.getDescription(), result.getDescription());
    }

    @Test
    void updateProject_WhenNotFound_ShouldThrowException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectService.updateProject(1L, projectDto));
    }

    @Test
    void deleteProject_WhenExists_ShouldDelete() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(1L);

        assertDoesNotThrow(() -> projectService.deleteProject(1L));
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void deleteProject_WhenNotExists_ShouldThrowException() {
        when(projectRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProject(1L));
    }
}
