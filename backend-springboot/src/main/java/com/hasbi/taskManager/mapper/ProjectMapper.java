package com.hasbi.taskManager.mapper;

import com.hasbi.taskManager.dto.ProjectDto;
import com.hasbi.taskManager.entity.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDto toDto(Project project);
    Project toEntity(ProjectDto dto);
}
