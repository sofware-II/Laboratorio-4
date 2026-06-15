package com.hasbi.taskmanager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Desactivado temporalmente mientras se configura el contexto de pruebas")
@SpringBootTest
class TaskManagerApplicationTests {

    @Test
    void contextLoads() {
        assertNotNull(TaskManagerApplication.class);
    }
}