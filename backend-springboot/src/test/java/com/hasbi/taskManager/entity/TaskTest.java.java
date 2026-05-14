package com.hasbi.taskManager.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import com.hasbi.taskManager.entity.Task;
import com.hasbi.taskManager.enums.TaskStatus;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
    }

    @Test
    void testSetAndGetTitle() {
        String title = "Tarea 1";
        task.setTitle(title);
        assertEquals(title, task.getTitle());
    }

    @Test
    void testSetAndGetDescription() {
        String desc = "Descripción de prueba";
        task.setDescription(desc);
        assertEquals(desc, task.getDescription());
    }

    @Test
    void testSetAndGetStatus() {
        TaskStatus status = TaskStatus.TODO;
        task.setStatus(status);
        assertEquals(status, task.getStatus());
    }
}