package com.hasbi.taskmanager.repository;

import com.hasbi.taskmanager.entity.Project;
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
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    // Helper method to eliminate duplication
    private Project createTestProject(String name, String description) {
        Project project = new Project();
        project.setName(name);
        project.setDescription(description);
        return project;
    }

    @Test
    void shouldSaveProjectAndAssignId() {
        // Arrange
        Project project = createTestProject("Proyecto 1", "Descripción 1");

        // Act
        Project savedProject = projectRepository.save(project);

        // Assert
        assertThat(savedProject.getId()).isNotNull();
        assertThat(savedProject.getName()).isEqualTo("Proyecto 1");
    }

    @Test
    void shouldFindProjectById() {
        // Arrange
        Project project = createTestProject("Proyecto Test", "Descripción Test");
        Project savedProject = projectRepository.save(project);

        // Act
        Optional<Project> result = projectRepository.findById(savedProject.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Proyecto Test");
        assertThat(result.get().getDescription()).isEqualTo("Descripción Test");
    }

    @Test
    void shouldFindAllProjects() {
        // Arrange
        Project project1 = createTestProject("Proyecto 1", "Descripción 1");
        Project project2 = createTestProject("Proyecto 2", "Descripción 2");
        projectRepository.save(project1);
        projectRepository.save(project2);

        // Act
        List<Project> projects = projectRepository.findAll();

        // Assert
        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getName)
                .containsExactlyInAnyOrder("Proyecto 1", "Proyecto 2");
    }

    @Test
    void shouldUpdateProject() {
        // Arrange
        Project project = createTestProject("Nombre original", "Descripción original");
        Project savedProject = projectRepository.save(project);

        savedProject.setName("Nombre actualizado");
        savedProject.setDescription("Descripción actualizada");

        // Act
        Project updatedProject = projectRepository.save(savedProject);
        Optional<Project> result = projectRepository.findById(updatedProject.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Nombre actualizado");
        assertThat(result.get().getDescription()).isEqualTo("Descripción actualizada");
    }

    @Test
    void shouldDeleteProject() {
        // Arrange
        Project project = createTestProject("Proyecto a eliminar", "Descripción");
        Project savedProject = projectRepository.save(project);
        Long projectId = savedProject.getId();

        // Act
        projectRepository.deleteById(projectId);
        Optional<Project> result = projectRepository.findById(projectId);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenProjectExistsById() {
        // Arrange
        Project project = createTestProject("Proyecto existente", "Descripción");
        Project savedProject = projectRepository.save(project);

        // Act
        boolean exists = projectRepository.existsById(savedProject.getId());

        // Assert
        assertThat(exists).isTrue();
    }
}
