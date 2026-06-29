package com.hasbi.taskmanager.validator;

import com.hasbi.taskmanager.enums.TaskPriority;
import com.hasbi.taskmanager.enums.TaskStatus;
import org.springframework.stereotype.Component;

@Component
public class TaskValidator {

    public TaskStatus validateAndGetStatus(String statusStr) {
        if (statusStr == null) return null;
        try {
            return TaskStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task status: " + statusStr);
        }
    }

    public TaskPriority validateAndGetPriority(String priorityStr) {
        if (priorityStr == null) return null;
        try {
            return TaskPriority.valueOf(priorityStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task priority: " + priorityStr);
        }
    }
}
