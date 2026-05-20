package com.hasbi.taskManager.repository;

import com.hasbi.taskManager.entity.Project;
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

    @Test
    void shouldFindProjectById() {
        Project project = new Project();
        project.setName("Proyecto Test");
        project.setDescription("Descripción Test");

        Project savedProject = projectRepository.save(project);

        Optional<Project> result = projectRepository.findById(savedProject.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Proyecto Test");
        assertThat(result.get().getDescription()).isEqualTo("Descripción Test");
    }

    @Test
    void shouldFindAllProjects() {
        Project project1 = new Project();
        project1.setName("Proyecto 1");
        project1.setDescription("Descripción 1");

        Project project2 = new Project();
        project2.setName("Proyecto 2");
        project2.setDescription("Descripción 2");

        projectRepository.save(project1);
        projectRepository.save(project2);

        List<Project> projects = projectRepository.findAll();

        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getName)
                .containsExactlyInAnyOrder("Proyecto 1", "Proyecto 2");
    }

    @Test
    void shouldUpdateProject() {
        Project project = new Project();
        project.setName("Nombre original");
        project.setDescription("Descripción original");

        Project savedProject = projectRepository.save(project);

        savedProject.setName("Nombre actualizado");
        savedProject.setDescription("Descripción actualizada");
        Project updatedProject = projectRepository.save(savedProject);

        Optional<Project> result = projectRepository.findById(updatedProject.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Nombre actualizado");
        assertThat(result.get().getDescription()).isEqualTo("Descripción actualizada");
    }

    @Test
    void shouldDeleteProject() {
        Project project = new Project();
        project.setName("Proyecto a eliminar");
        project.setDescription("Descripción");

        Project savedProject = projectRepository.save(project);
        Long projectId = savedProject.getId();

        projectRepository.deleteById(projectId);

        Optional<Project> result = projectRepository.findById(projectId);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnTrueWhenProjectExistsById() {
        Project project = new Project();
        project.setName("Proyecto existente");
        project.setDescription("Descripción");

        Project savedProject = projectRepository.save(project);

        boolean exists = projectRepository.existsById(savedProject.getId());

        assertThat(exists).isTrue();
    }
}
