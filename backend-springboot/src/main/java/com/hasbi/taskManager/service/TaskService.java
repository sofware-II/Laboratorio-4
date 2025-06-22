package com.hasbi.taskManager.service;

import com.hasbi.taskManager.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    List<TaskDto> getTasksByProjectId(Long projectId);
    TaskDto getTaskById(Long id);
    TaskDto updateTask(Long id, TaskDto taskDto);
    void deleteTask(Long id);
}
