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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test Suite para Actualización de Tareas
 * 
 * Casos de prueba para la funcionalidad de actualización de tareas:
 * TC01: Actualizar Título
 * TC02: Actualizar Descripción
 * TC03: Actualizar Estado (TODO → IN_PROGRESS)
 * TC04: Actualizar Prioridad (MEDIUM → HIGH)
 * TC05: Actualizar Múltiples Campos
 * TC06: Tarea No Encontrada
 * TC07: Estado Inválido
 * TC08: Prioridad Inválida
 * TC09: Campos Nulos No Actualizan
 * TC10: Actualización Completa
 * 
 * @author Task Manager Test Suite
 * @version 1.0
 */
@DisplayName("Task Update Service Tests")
public class TaskUpdateServiceTest {

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

    // ========== SetUp ==========
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicializar datos de prueba
        projectEntity = new Project();
        projectEntity.setId(10L);
        projectEntity.setName("Test Project");

        taskEntity = new Task();
        taskEntity.setId(1L);
        taskEntity.setTitle("Original Task");
        taskEntity.setDescription("Original Description");
        taskEntity.setStatus(TaskStatus.TODO);
        taskEntity.setPriority(TaskPriority.MEDIUM);
        taskEntity.setProject(projectEntity);

        taskDto = new TaskDto(1L, "Original Task", "Original Description", "TODO", "MEDIUM", 10L);
    }

    // ========== TC01: Actualizar Título ==========
    @Test
    @DisplayName("TC01: Actualizar solamente el título de una tarea existente")
    void testUpdateTask_OnlyTitle_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "New Title", null, null, null, null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("New Title");
        expectedUpdatedTask.setDescription("Original Description");
        expectedUpdatedTask.setStatus(TaskStatus.TODO);
        expectedUpdatedTask.setPriority(TaskPriority.MEDIUM);

        TaskDto expectedResultDto = new TaskDto(1L, "New Title", "Original Description", "TODO", "MEDIUM", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("New Title");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getStatus()).isEqualTo("TODO");
        assertThat(result.getPriority()).isEqualTo("MEDIUM");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC02: Actualizar Descripción ==========
    @Test
    @DisplayName("TC02: Actualizar solamente la descripción de una tarea existente")
    void testUpdateTask_OnlyDescription_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, "New Description", null, null, null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Original Task");
        expectedUpdatedTask.setDescription("New Description");
        expectedUpdatedTask.setStatus(TaskStatus.TODO);
        expectedUpdatedTask.setPriority(TaskPriority.MEDIUM);

        TaskDto expectedResultDto = new TaskDto(1L, "Original Task", "New Description", "TODO", "MEDIUM", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("New Description");
        assertThat(result.getTitle()).isEqualTo("Original Task");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC03: Actualizar Estado a IN_PROGRESS ==========
    @Test
    @DisplayName("TC03: Actualizar el estado de una tarea de TODO a IN_PROGRESS")
    void testUpdateTask_StatusToInProgress_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, "IN_PROGRESS", null, null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Original Task");
        expectedUpdatedTask.setDescription("Original Description");
        expectedUpdatedTask.setStatus(TaskStatus.IN_PROGRESS);
        expectedUpdatedTask.setPriority(TaskPriority.MEDIUM);

        TaskDto expectedResultDto = new TaskDto(1L, "Original Task", "Original Description", "IN_PROGRESS", "MEDIUM", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC04: Actualizar Prioridad a HIGH ==========
    @Test
    @DisplayName("TC04: Actualizar la prioridad de una tarea de MEDIUM a HIGH")
    void testUpdateTask_PriorityToHigh_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, "HIGH", null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Original Task");
        expectedUpdatedTask.setDescription("Original Description");
        expectedUpdatedTask.setStatus(TaskStatus.TODO);
        expectedUpdatedTask.setPriority(TaskPriority.HIGH);

        TaskDto expectedResultDto = new TaskDto(1L, "Original Task", "Original Description", "TODO", "HIGH", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPriority()).isEqualTo("HIGH");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC05: Actualizar Múltiples Campos ==========
    @Test
    @DisplayName("TC05: Actualizar título, descripción, estado y prioridad simultáneamente")
    void testUpdateTask_MultipleFields_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Updated Title", "Updated Description", "DONE", "LOW", null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Updated Title");
        expectedUpdatedTask.setDescription("Updated Description");
        expectedUpdatedTask.setStatus(TaskStatus.DONE);
        expectedUpdatedTask.setPriority(TaskPriority.LOW);

        TaskDto expectedResultDto = new TaskDto(1L, "Updated Title", "Updated Description", "DONE", "LOW", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Updated Title");
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getStatus()).isEqualTo("DONE");
        assertThat(result.getPriority()).isEqualTo("LOW");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC06: Tarea No Encontrada ==========
    @Test
    @DisplayName("TC06: Intentar actualizar una tarea que no existe lanza ResourceNotFoundException")
    void testUpdateTask_TaskNotFound_ThrowsException() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, "Updated Title", null, null, null, null);
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(999L, updateDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Task not found with ID: 999");

        verify(taskRepository).findById(999L);
        verify(taskRepository, never()).save(any());
    }

    // ========== TC07: Estado Inválido ==========
    @Test
    @DisplayName("TC07: Establecer un estado inválido lanza IllegalArgumentException")
    void testUpdateTask_InvalidStatus_ThrowsException() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, "INVALID_STATUS", null, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task status: INVALID_STATUS");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    // ========== TC08: Prioridad Inválida ==========
    @Test
    @DisplayName("TC08: Establecer una prioridad inválida lanza IllegalArgumentException")
    void testUpdateTask_InvalidPriority_ThrowsException() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, "INVALID_PRIORITY", null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(1L, updateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid task priority: INVALID_PRIORITY");

        verify(taskRepository).findById(1L);
        verify(taskRepository, never()).save(any());
    }

    // ========== TC09: Campos Nulos No Actualizan ==========
    @Test
    @DisplayName("TC09: Campos nulos no actualizan la tarea, valores originales se preservan")
    void testUpdateTask_NullFields_PreservesOriginalValues() {
        // Arrange
        TaskDto updateDto = new TaskDto(null, null, null, null, null, null);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Original Task");
        expectedUpdatedTask.setDescription("Original Description");
        expectedUpdatedTask.setStatus(TaskStatus.TODO);
        expectedUpdatedTask.setPriority(TaskPriority.MEDIUM);

        TaskDto expectedResultDto = new TaskDto(1L, "Original Task", "Original Description", "TODO", "MEDIUM", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Original Task");
        assertThat(result.getDescription()).isEqualTo("Original Description");
        assertThat(result.getStatus()).isEqualTo("TODO");
        assertThat(result.getPriority()).isEqualTo("MEDIUM");

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TC10: Actualización Completa del Objeto ==========
    @Test
    @DisplayName("TC10: Actualización completa con todos los campos válidos")
    void testUpdateTask_AllValidFields_Success() {
        // Arrange
        TaskDto updateDto = new TaskDto(1L, "Completely Updated", "Brand new description", "IN_PROGRESS", "HIGH", 10L);
        Task expectedUpdatedTask = new Task();
        expectedUpdatedTask.setId(1L);
        expectedUpdatedTask.setTitle("Completely Updated");
        expectedUpdatedTask.setDescription("Brand new description");
        expectedUpdatedTask.setStatus(TaskStatus.IN_PROGRESS);
        expectedUpdatedTask.setPriority(TaskPriority.HIGH);
        expectedUpdatedTask.setProject(projectEntity);

        TaskDto expectedResultDto = new TaskDto(1L, "Completely Updated", "Brand new description", "IN_PROGRESS", "HIGH", 10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(any(Task.class))).thenReturn(expectedUpdatedTask);
        when(taskMapper.toDto(expectedUpdatedTask)).thenReturn(expectedResultDto);

        // Act
        TaskDto result = taskService.updateTask(1L, updateDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Completely Updated");
        assertThat(result.getDescription()).isEqualTo("Brand new description");
        assertThat(result.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(result.getPriority()).isEqualTo("HIGH");
        assertThat(result.getProjectId()).isEqualTo(10L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(any(Task.class));
    }

    // ========== TearDown ==========
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Limpiar mocks después de cada prueba
        reset(taskRepository, projectRepository, taskMapper);
    }
}
