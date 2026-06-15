package com.hasbi.taskmanager.entity;

import com.hasbi.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void shouldAssignTitleToTask() {
        Task task = new Task();

        task.setTitle("Test Task");

        assertThat(task.getTitle()).isEqualTo("Test Task");
    }

    @Test
    void shouldAssignDescriptionToTask() {
        Task task = new Task();

        task.setDescription("Test Desc");

        assertThat(task.getDescription()).isEqualTo("Test Desc");
    }

    @Test
    void shouldAssignValidStatusToTask() {
        Task task = new Task();

        task.setStatus(TaskStatus.TODO);

        assertThat(task.getStatus()).isEqualTo(TaskStatus.TODO);
    }
}