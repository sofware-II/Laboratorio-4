package com.hasbi.taskManager.repository;

import com.hasbi.taskManager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}
