package com.hasbi.taskmanager.mapper;

import com.hasbi.taskmanager.dto.ProjectDto;
import com.hasbi.taskmanager.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDto toDto(Project project);
    Project toEntity(ProjectDto dto);
}
