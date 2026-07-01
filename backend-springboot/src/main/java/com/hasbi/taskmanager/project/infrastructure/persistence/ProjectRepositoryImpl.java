package com.hasbi.taskmanager.project.infrastructure.persistence;

import com.hasbi.taskmanager.project.domain.model.Project;
import com.hasbi.taskmanager.project.domain.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {
    private final SpringDataProjectRepository springDataProjectRepository;

    @Override
    public Project save(Project project) {
        return springDataProjectRepository.save(project);
    }

    @Override
    public List<Project> findAll() {
        return springDataProjectRepository.findAll();
    }

    @Override
    public Optional<Project> findById(Long id) {
        return springDataProjectRepository.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataProjectRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        springDataProjectRepository.deleteById(id);
    }
}
