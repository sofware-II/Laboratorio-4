package com.hasbi.taskManager.service;

import com.hasbi.taskManager.dto.TaskDto;
import com.hasbi.taskManager.entity.Project;
import com.hasbi.taskManager.entity.Task;
import com.hasbi.taskManager.enums.TaskPriority;
import com.hasbi.taskManager.enums.TaskStatus;
import com.hasbi.taskManager.exception.ResourceNotFoundException;
import com.hasbi.taskManager.mapper.TaskMapper;
import com.hasbi.taskManager.repository.ProjectRepository;
import com.hasbi.taskManager.repository.TaskRepository;
import com.hasbi.taskManager.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private TaskMapper taskMapper;

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
}
