package com.hasbi.taskmanager.service;

import com.hasbi.taskmanager.dto.TaskDto;
import com.hasbi.taskmanager.entity.Project;
import com.hasbi.taskmanager.entity.Task;
import com.hasbi.taskmanager.enums.TaskPriority;
import com.hasbi.taskmanager.validator.TaskValidator;
import com.hasbi.taskmanager.enums.TaskStatus;
import com.hasbi.taskmanager.exception.ResourceNotFoundException;
import com.hasbi.taskmanager.mapper.TaskMapper;
import com.hasbi.taskmanager.repository.ProjectRepository;
import com.hasbi.taskmanager.repository.TaskRepository;
import com.hasbi.taskmanager.service.impl.TaskServiceImpl;
import com.hasbi.taskmanager.validator.TaskValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private TaskValidator taskValidator;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Project projectEntity;
    private Task taskEntity;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        projectEntity = new Project();
        projectEntity.setId(10L);

        taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setTitle("Test Task");
        taskEntity.setDescription("Test Desc");
        taskEntity.setStatus(TaskStatus.TODO);
        taskEntity.setPriority(TaskPriority.MEDIUM);
        taskEntity.setProject(projectEntity);

        taskDto = new TaskDto(1L, "Test Task", "Test Desc", "TODO", "MEDIUM", 10L);
    }

    @Test
    void testCreateTask_Success() {
        when(projectRepository.findById(10L)).thenReturn(Optional.of(projectEntity));
        when(taskMapper.toEntity(taskDto)).thenReturn(taskEntity);
        when(taskRepository.save(taskEntity)).thenReturn(taskEntity);
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDto);

        TaskDto created = taskService.createTask(taskDto);

        assertThat(created).isNotNull();
        assertThat(created.getId()).isEqualTo(1L);
        verify(projectRepository).findById(10L);
        verify(taskRepository).save(taskEntity);
    }

    @Test
    void testCreateTask_ProjectNotFound() {
        when(projectRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(taskDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Project not found with ID: 10");

        verify(projectRepository).findById(10L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void testGetTasksByProjectId() {
        when(taskRepository.findByProjectId(10L)).thenReturn(List.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDto);

        List<TaskDto> tasks = taskService.getTasksByProjectId(10L);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findByProjectId(10L);
    }

    @Test
    void testGetTaskById_Found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(taskRepository).findById(1L);
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 1");

        verify(taskRepository).findById(1L);
    }

    @Test
    void testUpdateTask_Success() {
        TaskDto updateDto = new TaskDto(null, "Updated Task", null, "IN_PROGRESS", "HIGH", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskValidator.validateAndGetStatus("IN_PROGRESS")).thenReturn(TaskStatus.IN_PROGRESS);
        when(taskValidator.validateAndGetPriority("HIGH")).thenReturn(TaskPriority.HIGH);
        when(taskRepository.save(any(Task.class))).thenReturn(taskEntity);
        when(taskMapper.toDto(taskEntity)).thenReturn(taskDto);

        TaskDto updated = taskService.updateTask(1L, updateDto);

        assertThat(updated).isNotNull();
        verify(taskRepository).save(taskEntity);
    }

    @Test
    void testUpdateTask_InvalidStatus() {
        TaskDto updateDto = new TaskDto(null, null, null, "INVALID_STATUS", null, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskValidator.validateAndGetStatus("INVALID_STATUS"))
            .thenThrow(new IllegalArgumentException("Invalid task status: INVALID_STATUS"));

        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task status: INVALID_STATUS");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void testUpdateTask_InvalidPriority() {
        TaskDto updateDto = new TaskDto(null, null, null, "TODO", "INVALID_PRIORITY", null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskValidator.validateAndGetStatus("TODO")).thenReturn(TaskStatus.TODO);
        when(taskValidator.validateAndGetPriority("INVALID_PRIORITY"))
            .thenThrow(new IllegalArgumentException("Invalid task priority: INVALID_PRIORITY"));

        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task priority: INVALID_PRIORITY");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> taskService.deleteTask(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 1");

        verify(taskRepository).existsById(1L);
    }

    @Test
    void testUpdateTask_NotFound() {
    TaskDto updateDto = new TaskDto(null, "Updated Task", null, "IN_PROGRESS", "HIGH", 10L);

    when(taskRepository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> taskService.updateTask(99L, updateDto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Task not found with ID: 99");

    verify(taskRepository).findById(99L);
    verify(taskRepository, never()).save(any());
    }

    @Test
    void testGetTasksByProjectId_EmptyList() {
    when(taskRepository.findByProjectId(10L)).thenReturn(List.of());

    List<TaskDto> tasks = taskService.getTasksByProjectId(10L);

    assertThat(tasks).isEmpty();

    verify(taskRepository).findByProjectId(10L);
    verify(taskMapper, never()).toDto(any(Task.class));
    }
}


    