package com.hasbi.taskmanager.project.presentation;

import com.hasbi.taskmanager.project.application.dto.ProjectDto;
import com.hasbi.taskmanager.project.application.service.ProjectService;
import com.hasbi.taskmanager.controller.ApiPaths;
import com.hasbi.taskmanager.controller.ControllerResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.PROJECTS)
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping(ApiPaths.PROJECTS_ADD)
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody ProjectDto projectDto) {
        return ControllerResponseBuilder.ok(projectService.createProject(projectDto));
    }

    @GetMapping(ApiPaths.PROJECTS_ALL)
    public ResponseEntity<List<ProjectDto>> getAllProjects() {
        return ControllerResponseBuilder.ok(projectService.getAllProjects());
    }

    @GetMapping(ApiPaths.PROJECTS_BY_ID)
    public ResponseEntity<ProjectDto> getProjectById(@PathVariable(ApiPaths.ID) Long id) {
        return ControllerResponseBuilder.ok(projectService.getProjectById(id));
    }

    @PutMapping(ApiPaths.PROJECTS_UPDATE)
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable(ApiPaths.ID) Long id,
            @Valid @RequestBody ProjectDto projectDto) {
        return ControllerResponseBuilder.ok(projectService.updateProject(id, projectDto));
    }

    @DeleteMapping(ApiPaths.PROJECTS_DELETE)
    public ResponseEntity<Void> deleteProject(@PathVariable(ApiPaths.ID) Long id) {
        projectService.deleteProject(id);
        return ControllerResponseBuilder.noContent();
    }
}
