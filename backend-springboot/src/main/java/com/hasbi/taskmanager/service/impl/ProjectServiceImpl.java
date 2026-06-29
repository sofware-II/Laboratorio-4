package com.hasbi.taskmanager.service.impl;

import com.hasbi.taskmanager.dto.ProjectDto;
import com.hasbi.taskmanager.entity.Project;
import com.hasbi.taskmanager.exception.ResourceNotFoundException;
import com.hasbi.taskmanager.mapper.ProjectMapper;
import com.hasbi.taskmanager.repository.ProjectRepository;
import com.hasbi.taskmanager.service.ProjectService;
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
public class ProjectServiceImpl implements ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private static final String PROJECT_NOT_FOUND_MESSAGE = "Project not found with ID: ";

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        Project savedProject = projectRepository.save(project);

        logger.info("Created project with ID {}", savedProject.getId());

        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projects = mapProjectsToDtos(projectRepository.findAll());

        logger.info("Fetched {} projects", projects.size());

        return projects;
    }

    @Override
    public ProjectDto getProjectById(Long projectId) {
        Project project = findProjectById(projectId);

        logger.info("Retrieved project with ID {}", projectId);

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProject(Long projectId, ProjectDto projectDto) {
        Project existingProject = findProjectById(projectId);

     updateProjectFields(existingProject, projectDto);

        Project updatedProject = projectRepository.save(existingProject);

        logger.info("Updated project ID {}", updatedProject.getId());

        return projectMapper.toDto(updatedProject);
    }

    private void updateProjectProperties(Project existingProject, ProjectDto projectDto) {
        if (projectDto.getName() != null) {
            existingProject.setName(projectDto.getName());
        }
        if (projectDto.getDescription() != null) {
            existingProject.setDescription(projectDto.getDescription());
        }
    }

    @Override
    public void deleteProject(Long projectId) {
        validateProjectExists(projectId);

        projectRepository.deleteById(projectId);

        logger.info("Deleted project ID {}", projectId);
    }

    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + projectId));
    }

    private void validateProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException(PROJECT_NOT_FOUND_MESSAGE + projectId);
        }
    }

    private List<ProjectDto> mapProjectsToDtos(List<Project> projects) {
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    private void updateProjectFields(Project existingProject, ProjectDto projectDto) {
        updateProjectName(existingProject, projectDto);
        updateProjectDescription(existingProject, projectDto);
    }

    private void updateProjectName(Project existingProject, ProjectDto projectDto) {
        if (projectDto.getName() != null) {
            existingProject.setName(projectDto.getName());
        }
    }

    private void updateProjectDescription(Project existingProject, ProjectDto projectDto) {
        if (projectDto.getDescription() != null) {
            existingProject.setDescription(projectDto.getDescription());
        }
    }
}