import React from 'react';
import { useNavigate } from 'react-router-dom';
import ProjectsList from '../components/ProjectsList';

const ProjectsPage = () => {
  const navigate = useNavigate();

  const handleProjectSelect = (id) => {
    navigate(`/projects/${id}/tasks`);
  };

  return (
    <div>
        <h2>Projects</h2>
        <div className="container">
            <ProjectsList onSelect={handleProjectSelect} />
        </div>
    </div>
  );
};

export default ProjectsPage;
