package com.hasbi.taskmanager.project.infrastructure.persistence;

import com.hasbi.taskmanager.project.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProjectRepository extends JpaRepository<Project, Long> {
}
