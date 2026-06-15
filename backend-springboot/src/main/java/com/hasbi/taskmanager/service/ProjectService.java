package com.hasbi.taskmanager.service;

import com.hasbi.taskmanager.dto.ProjectDto;

import java.util.List;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    List<ProjectDto> getAllProjects();
    ProjectDto getProjectById(Long projectId);
    ProjectDto updateProject(Long projectId, ProjectDto projectDto);
    void deleteProject(Long projectId);
}