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

    @Test
    void shouldFindTaskById() {
        Project project = new Project();
        project.setName("Proyecto Test");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Desc");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProject(savedProject);

        Task savedTask = taskRepository.save(task);

        Optional<Task> result = taskRepository.findById(savedTask.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void shouldFindTasksByProjectId() {
        Project project = new Project();
        project.setName("Proyecto Test");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Task by Project");
        task.setDescription("Task Desc");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProject(savedProject);

        taskRepository.save(task);

        List<Task> tasks = taskRepository.findByProjectId(savedProject.getId());

        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Task by Project");
    }

    @Test
    void shouldReturnTrueWhenTaskExistsById() {
        Project project = new Project();
        project.setName("Proyecto Test");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Existing Task");
        task.setDescription("Existing Desc");
        task.setStatus(TaskStatus.TODO);
        task.setPriority(TaskPriority.MEDIUM);
        task.setProject(savedProject);

        Task savedTask = taskRepository.save(task);

        boolean exists = taskRepository.existsById(savedTask.getId());

        assertThat(exists).isTrue();
    }
}
