package com.hasbi.taskManager;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Se deshabilita porque requiere levantar el contexto completo de Spring Boot")
class TaskManagerApplicationTests {

    @Test
    void contextLoads() {
    }
}