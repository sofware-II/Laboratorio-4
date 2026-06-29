package com.hasbi.taskmanager.service;

import com.hasbi.taskmanager.dto.TaskDto;

import java.util.List;

public interface TaskService {
    TaskDto createTask(TaskDto taskDto);
    List<TaskDto> getTasksByProjectId(Long projectId);
    TaskDto getTaskById(Long taskId);
    TaskDto updateTask(Long taskId, TaskDto taskDto);
    void deleteTask(Long taskId);
}