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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskUpdateTest {

    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProjectRepository projectRepository;

    private Project projectEntity;
    private Task existingTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializar objetos comunes para evitar duplicación
        projectEntity = new Project();
        projectEntity.setId(10L);
        projectEntity.setName("Test Project");

        existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setStatus(TaskStatus.TODO);
        existingTask.setPriority(TaskPriority.LOW);
        existingTask.setProject(projectEntity);
    }

    /**
     * CASO 1: Actualizar todos los campos de la tarea exitosamente
     * Verifica que todos los campos (título, descripción, estado, prioridad) se actualicen correctamente.
     */
    @Test
    void testUpdateTask_AllFieldsSuccess() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, "Updated Title", "Updated Description", 
                TaskStatus.IN_PROGRESS.name(), TaskPriority.HIGH.name(), 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("Updated Description");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setPriority(TaskPriority.HIGH);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "Updated Title", "Updated Description", 
                TaskStatus.IN_PROGRESS.name(), TaskPriority.HIGH.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS.name());
        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    /**
     * CASO 2: Actualizar únicamente el título, dejando el resto de campos intactos
     * Verifica que solo el título cambie mientras los demás campos se preserven.
     */
    @Test
    void testUpdateTask_OnlyTitleSuccess() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, "New Title Only", null, null, null, 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("New Title Only");
        updatedTask.setDescription("Original Description");
        updatedTask.setStatus(TaskStatus.TODO);
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "New Title Only", "Original Description", 
                TaskStatus.TODO.name(), TaskPriority.LOW.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title Only");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO.name());
        assertThat(result.getPriority()).isEqualTo(TaskPriority.LOW.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    /**
     * CASO 3: Actualizar únicamente la descripción, dejando los demás campos intactos
     * Verifica que solo la descripción cambie mientras el resto se preserva.
     */
    @Test
    void testUpdateTask_OnlyDescriptionSuccess() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, null, "New Description Only", null, null, 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Original Title");
        updatedTask.setDescription("New Description Only");
        updatedTask.setStatus(TaskStatus.TODO);
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "Original Title", "New Description Only", 
                TaskStatus.TODO.name(), TaskPriority.LOW.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Original Title");
        assertThat(result.getDescription()).isEqualTo("New Description Only");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO.name());
        assertThat(result.getPriority()).isEqualTo(TaskPriority.LOW.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    /**
     * CASO 4: Actualizar únicamente el estado de la tarea
     * Verifica que el estado se actualice correctamente a IN_PROGRESS.
     */
    @Test
    void testUpdateTask_OnlyStatusSuccess() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, null, null, TaskStatus.IN_PROGRESS.name(), null, 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Original Title");
        updatedTask.setDescription("Original Description");
        updatedTask.setStatus(TaskStatus.IN_PROGRESS);
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "Original Title", "Original Description", 
                TaskStatus.IN_PROGRESS.name(), TaskPriority.LOW.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS.name());
        assertThat(result.getTitle()).isEqualTo("Original Title");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getPriority()).isEqualTo(TaskPriority.LOW.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    /**
     * CASO 5: Actualizar únicamente la prioridad de la tarea
     * Verifica que la prioridad se actualice correctamente a HIGH.
     */
    @Test
    void testUpdateTask_OnlyPrioritySuccess() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, null, null, null, TaskPriority.HIGH.name(), 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Original Title");
        updatedTask.setDescription("Original Description");
        updatedTask.setStatus(TaskStatus.TODO);
        updatedTask.setPriority(TaskPriority.HIGH);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "Original Title", "Original Description", 
                TaskStatus.TODO.name(), TaskPriority.HIGH.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPriority()).isEqualTo(TaskPriority.HIGH.name());
        assertThat(result.getTitle()).isEqualTo("Original Title");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getStatus()).isEqualTo(TaskStatus.TODO.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }

    /**
     * CASO 6: Intentar actualizar una tarea cuyo ID no existe en el sistema
     * Verifica que se lance la excepción ResourceNotFoundException cuando el ID no existe.
     */
    @Test
    void testUpdateTask_TaskNotFound() {
        // Arrange
        TaskDto updateDto = new TaskDto(99L, "Updated Title", "Updated Description", 
                TaskStatus.IN_PROGRESS.name(), TaskPriority.HIGH.name(), 10L);

        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(99L, updateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 99");

        verify(taskRepository).findById(99L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * CASO 7: Intentar actualizar con un estado inválido
     * Verifica que se lance IllegalArgumentException cuando el estado es inválido.
     */
    @Test
    void testUpdateTask_InvalidStatus() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, "Updated Title", "Updated Description", 
                "INVALID_STATUS", TaskPriority.HIGH.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task status");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * CASO 8: Intentar actualizar con una prioridad inválida
     * Verifica que se lance IllegalArgumentException cuando la prioridad es inválida.
     */
    @Test
    void testUpdateTask_InvalidPriority() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, "Updated Title", "Updated Description", 
                TaskStatus.IN_PROGRESS.name(), "INVALID_PRIORITY", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task priority");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    /**
     * CASO 9: Marcar una tarea directamente como completada (DONE)
     * Verifica el flujo de negocio al cambiar el estado a DONE.
     */
    @Test
    void testUpdateTask_MarkAsDone() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, null, null, TaskStatus.DONE.name(), null, 10L);

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Original Title");
        updatedTask.setDescription("Original Description");
        updatedTask.setStatus(TaskStatus.DONE);
        updatedTask.setPriority(TaskPriority.LOW);
        updatedTask.setProject(projectEntity);

        TaskDto resultDto = new TaskDto(1L, "Original Title", "Original Description", 
                TaskStatus.DONE.name(), TaskPriority.LOW.name(), 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toDto(updatedTask)).thenReturn(resultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(TaskStatus.DONE.name());
        assertThat(result.getTitle()).isEqualTo("Original Title");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getPriority()).isEqualTo(TaskPriority.LOW.name());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toDto(updatedTask);
    }
}
