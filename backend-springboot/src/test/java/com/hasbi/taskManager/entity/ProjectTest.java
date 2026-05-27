package com.hasbi.taskManager.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    @Test
    void shouldAssignNameToProject() {
        Project project = new Project();

        project.setName("Proyecto 1");

        assertThat(project.getName()).isEqualTo("Proyecto 1");
    }

    @Test
    void shouldAssignDescriptionToProject() {
        Project project = new Project();

        project.setDescription("Descripción 1");

        assertThat(project.getDescription()).isEqualTo("Descripción 1");
    }

    @Test
    void shouldAssignIdToProject() {
        Project project = new Project();

        project.setId(1L);

        assertThat(project.getId()).isEqualTo(1L);
    }
}
