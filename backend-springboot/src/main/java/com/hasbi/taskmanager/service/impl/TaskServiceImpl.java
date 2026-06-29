package com.hasbi.taskmanager.service.impl;

import com.hasbi.taskmanager.dto.TaskDto;
import com.hasbi.taskmanager.entity.Project;
import com.hasbi.taskmanager.entity.Task;
import com.hasbi.taskmanager.enums.TaskPriority;
import com.hasbi.taskmanager.enums.TaskStatus;
import com.hasbi.taskmanager.exception.ResourceNotFoundException;
import com.hasbi.taskmanager.mapper.TaskMapper;
import com.hasbi.taskmanager.repository.ProjectRepository;
import com.hasbi.taskmanager.repository.TaskRepository;
import com.hasbi.taskmanager.service.TaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found with ID: ";
    private static final String TASK_NOT_FOUND_MESSAGE = "Task not found with ID: ";
    private static final String INVALID_TASK_STATUS_MESSAGE = "Invalid task status: ";
    private static final String INVALID_TASK_PRIORITY_MESSAGE = "Invalid task priority: ";

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Project project = findProjectById(taskDto.getProjectId());

        Task taskEntity = taskMapper.toEntity(taskDto);
        taskEntity.setProject(project);

        Task savedTask = taskRepository.save(taskEntity);

        logger.info("Created task with ID {} for project ID {}", savedTask.getId(), project.getId());

        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        List<TaskDto> tasks = mapTasksToDtos(taskRepository.findByProjectId(projectId));

        logger.info("Fetched {} tasks for project ID {}", tasks.size(), projectId);

        return tasks;
    }

    @Override
    public TaskDto getTaskById(Long taskId) {
        Task task = findTaskById(taskId);

        logger.info("Retrieved task with ID {}", taskId);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto updateTask(Long taskId, TaskDto taskDto) {
        Task existingTask = findTaskById(taskId);

        updateTaskFields(existingTask, taskDto);

        Task updatedTask = taskRepository.save(existingTask);

        logger.info("Updated task ID {}", updatedTask.getId());

        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long taskId) {
        validateTaskExists(taskId);

        taskRepository.deleteById(taskId);

        logger.info("Deleted task ID {}", taskId);
    }

    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + projectId));
    }

    private Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE + taskId));
    }

    private void validateTaskExists(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new ResourceNotFoundException(TASK_NOT_FOUND_MESSAGE + taskId);
        }
    }

    private List<TaskDto> mapTasksToDtos(List<Task> tasks) {
        return tasks.stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    private void updateTaskFields(Task existingTask, TaskDto taskDto) {
        updateTaskTitle(existingTask, taskDto);
        updateTaskDescription(existingTask, taskDto);
        updateTaskStatus(existingTask, taskDto);
        updateTaskPriority(existingTask, taskDto);
    }

    private void updateTaskTitle(Task existingTask, TaskDto taskDto) {
        if (taskDto.getTitle() != null) {
            existingTask.setTitle(taskDto.getTitle());
        }
    }

    private void updateTaskDescription(Task existingTask, TaskDto taskDto) {
        if (taskDto.getDescription() != null) {
            existingTask.setDescription(taskDto.getDescription());
        }
    }

    private void updateTaskStatus(Task existingTask, TaskDto taskDto) {
        if (taskDto.getStatus() != null) {
            existingTask.setStatus(parseTaskStatus(taskDto.getStatus()));
        }
    }

    private void updateTaskPriority(Task existingTask, TaskDto taskDto) {
        if (taskDto.getPriority() != null) {
            existingTask.setPriority(parseTaskPriority(taskDto.getPriority()));
        }
    }

    private TaskStatus parseTaskStatus(String status) {
        try {
            return TaskStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_TASK_STATUS_MESSAGE + status);
        }
    }

    private TaskPriority parseTaskPriority(String priority) {
        try {
            return TaskPriority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(INVALID_TASK_PRIORITY_MESSAGE + priority);
        }
    }
}