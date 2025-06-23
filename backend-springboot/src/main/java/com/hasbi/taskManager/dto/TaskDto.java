package com.hasbi.taskManager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    private String description;
    @NotBlank(message = "Status is required")
    private String status;
    private String priority;
    private Long projectId;
}
