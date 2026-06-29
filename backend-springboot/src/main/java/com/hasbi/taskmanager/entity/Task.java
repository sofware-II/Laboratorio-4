package com.hasbi.taskmanager.entity;

import com.hasbi.taskmanager.enums.TaskPriority;
import com.hasbi.taskmanager.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data  
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Patrón: Replace Constructor with Factory Method
    public static Task createNewTask(String title, String description, Project project) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProject(project);
        return task;
    }
}
