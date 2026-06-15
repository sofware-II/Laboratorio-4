package com.hasbi.taskmanager.mapper;

import com.hasbi.taskmanager.dto.TaskDto;
import com.hasbi.taskmanager.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "project.id", target = "projectId")
    TaskDto toDto(Task task);

    @Mapping(source = "projectId", target = "project.id")
    Task toEntity(TaskDto dto);
}
