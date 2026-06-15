package com.hasbi.taskmanager.repository;

import com.hasbi.taskmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Long> {
}
