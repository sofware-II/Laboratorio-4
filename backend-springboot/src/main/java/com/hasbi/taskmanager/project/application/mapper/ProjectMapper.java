package com.hasbi.taskmanager.project.application.mapper;

import com.hasbi.taskmanager.project.application.dto.ProjectDto;
import com.hasbi.taskmanager.project.domain.model.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectDto dto);
    
    ProjectDto toDto(Project project);
}
