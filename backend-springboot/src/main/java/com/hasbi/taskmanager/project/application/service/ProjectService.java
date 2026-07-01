package com.hasbi.taskmanager.project.application.service;

import com.hasbi.taskmanager.project.application.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long id);
    ProjectDto updateProject(Long id, ProjectDto projectDto);
    void deleteProject(Long id);
}
