package com.hasbi.taskmanager.mapper;

import com.hasbi.taskmanager.dto.ProjectDto;
import com.hasbi.taskmanager.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    Project toEntity(ProjectDto dto);
    ProjectDto toDto(Project project);
}
