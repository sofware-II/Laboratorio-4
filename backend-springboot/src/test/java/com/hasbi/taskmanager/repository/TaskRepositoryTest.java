package com.hasbi.taskmanager.repository;

import com.hasbi.taskmanager.entity.Project;
import com.hasbi.taskmanager.entity.Task;
import com.hasbi.taskmanager.enums.TaskPriority;
import com.hasbi.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Helper methods to eliminate duplication
    private Project createTestProject(String name) {
        Project project = new Project();
        project.setName(name);
        return projectRepository.save(project);
    }

    private Task createTestTask(String title, String description, Project project) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProject(project);
        return task;
    }

    @Test
    void shouldFindTaskById() {
        // Arrange
        Project savedProject = createTestProject("Proyecto Test");
        Task task = createTestTask("Test Task", "Test Desc", savedProject);
        Task savedTask = taskRepository.save(task);

        // Act
        Optional<Task> result = taskRepository.findById(savedTask.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void shouldFindTasksByProjectId() {
        // Arrange
        Project savedProject = createTestProject("Proyecto Test");
        Task task = createTestTask("Task by Project", "Task Desc", savedProject);
        taskRepository.save(task);

        // Act
        List<Task> tasks = taskRepository.findByProjectId(savedProject.getId());

        // Assert
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task by Project");
    }

    @Test
    void shouldReturnTrueWhenTaskExistsById() {
        // Arrange
        Project savedProject = createTestProject("Proyecto Test");
        Task task = createTestTask("Existing Task", "Existing Desc", savedProject);
        Task savedTask = taskRepository.save(task);

        // Act
        boolean exists = taskRepository.existsById(savedTask.getId());

        // Assert
        assertThat(exists).isTrue();
    }
}
