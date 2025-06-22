package com.hasbi.taskManager.service.impl;

import com.hasbi.taskManager.dto.ProjectDto;
import com.hasbi.taskManager.entity.Project;
import com.hasbi.taskManager.exception.ResourceNotFoundException;
import com.hasbi.taskManager.mapper.ProjectMapper;
import com.hasbi.taskManager.repository.ProjectRepository;
import com.hasbi.taskManager.service.ProjectService;
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
    private ProjectRepository projectRepository;
    private ProjectMapper projectMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);


    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = projectMapper.toEntity(projectDto);
        Project savedProject = projectRepository.save(project);

        logger.info("Created project with ID {}", savedProject.getId());

        return projectMapper.toDto(savedProject);
    }

    @Override
    public List<ProjectDto> getAllProjects() {
        List<ProjectDto> projects = projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());

        logger.info("Fetched {} projects", projects.size());

        return projects;
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        logger.info("Retrieved project with ID {}", id);

        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectDto projectDto) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        if (projectDto.getName() != null) {
            existingProject.setName(projectDto.getName());
        }
        if (projectDto.getDescription() != null) {
            existingProject.setDescription(projectDto.getDescription());
        }

        Project updated = projectRepository.save(existingProject);

        logger.info("Updated project ID {}", updated.getId());

        return projectMapper.toDto(updated);
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);

        logger.info("Deleted project ID {}", id);
    }
}
