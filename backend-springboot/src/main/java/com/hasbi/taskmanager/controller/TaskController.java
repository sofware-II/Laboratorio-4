package com.hasbi.taskmanager.controller;

import com.hasbi.taskmanager.dto.TaskDto;
import com.hasbi.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.TASKS)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping(ApiPaths.TASKS_ADD)
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody TaskDto taskDto) {
        return ControllerResponseBuilder.ok(taskService.createTask(taskDto));
    }

    @GetMapping(ApiPaths.TASKS_BY_PROJECT)
    public ResponseEntity<List<TaskDto>> getTasksByProjectId(
            @PathVariable(ApiPaths.PROJECT_ID) Long projectId) {
        return ControllerResponseBuilder.ok(taskService.getTasksByProjectId(projectId));
    }

    @GetMapping(ApiPaths.TASKS_BY_ID)
    public ResponseEntity<TaskDto> getTaskById(@PathVariable(ApiPaths.ID) Long id) {
        return ControllerResponseBuilder.ok(taskService.getTaskById(id));
    }

    @PutMapping(ApiPaths.TASKS_UPDATE)
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable(ApiPaths.ID) Long id,
            @Valid @RequestBody TaskDto taskDto) {
        return ControllerResponseBuilder.ok(taskService.updateTask(id, taskDto));
    }

    @DeleteMapping(ApiPaths.TASKS_DELETE)
    public ResponseEntity<Void> deleteTask(@PathVariable(ApiPaths.ID) Long id) {
        taskService.deleteTask(id);
        return ControllerResponseBuilder.noContent();
    }
}
