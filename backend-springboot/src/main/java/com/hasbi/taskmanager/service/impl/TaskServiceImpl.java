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
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;
    private TaskMapper taskMapper;

    private TaskValidator taskValidator;

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Project project = projectRepository.findById(taskDto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + taskDto.getProjectId()));

        Task taskEntity = taskMapper.toEntity(taskDto);
        taskEntity.setProject(project);

        Task savedTask = taskRepository.save(taskEntity);

        logger.info("Created task with ID {} for project ID {}", savedTask.getId(), project.getId());

        return taskMapper.toDto(savedTask);
    }

    @Override
    public List<TaskDto> getTasksByProjectId(Long projectId) {
        List<TaskDto> tasks = taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());

        logger.info("Fetched {} tasks for project ID {}", tasks.size(), projectId);

        return tasks;
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        logger.info("Retrieved task with ID {}", id);

        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto updateTask(Long id, TaskDto taskDto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        updateTaskProperties(existingTask, taskDto);

        Task updated = taskRepository.save(existingTask);

        logger.info("Updated task ID {}", updated.getId());

        return taskMapper.toDto(updated);
    }

    private void updateTaskProperties(Task existingTask, TaskDto taskDto) {
        if (taskDto.getTitle() != null) {
            existingTask.setTitle(taskDto.getTitle());
        }
        if (taskDto.getDescription() != null) {
            existingTask.setDescription(taskDto.getDescription());
        }
        if (taskDto.getStatus() != null) {
            existingTask.setStatus(taskValidator.validateAndGetStatus(taskDto.getStatus()));
        }
        if (taskDto.getPriority() != null) {
            existingTask.setPriority(taskValidator.validateAndGetPriority(taskDto.getPriority()));
        }
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with ID: " + id);
        }
        taskRepository.deleteById(id);

        logger.info("Deleted task ID {}", id);
    }
}
