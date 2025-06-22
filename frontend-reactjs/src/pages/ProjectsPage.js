import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ProjectsList from '../components/ProjectsList';
import ConfirmModal from '../components/ConfirmModal';
import { fetchProjects, deleteProject } from '../api/projects';

const ProjectsPage = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);

  const loadProjects = () => {
    fetchProjects()
      .then((res) => setProjects(res.data))
      .catch(console.error);
  };

  useEffect(() => {
    loadProjects();
  }, []);

  const handleProjectSelect = (id) => {
    navigate(`/projects/${id}/tasks`);
  };

  const handleEditClick = (project) => {
    console.log('Edit project:', project);
    // You can open a form here
  };

  const handleDeleteClick = (project) => {
    setSelectedProject(project);
    setModalOpen(true);
  };

  const confirmDelete = () => {
    if (!selectedProject) return;
    deleteProject(selectedProject.id)
      .then(() => {
        loadProjects();
        setModalOpen(false);
        setSelectedProject(null);
      })
      .catch(console.error);
  };

  const cancelDelete = () => {
    setModalOpen(false);
    setSelectedProject(null);
  };

  return (
    <div>
      <h2>Projects</h2>
      <div className="container">
        <ProjectsList
          onSelect={handleProjectSelect}
          onEdit={handleEditClick}
          onDelete={handleDeleteClick}
        />
        <ConfirmModal
          show={modalOpen}
          title="Confirm Deletion"
          message={`Are you sure you want to delete project "${selectedProject?.name}"?`}
          onConfirm={confirmDelete}
          onCancel={cancelDelete}
        />
      </div>
    </div>
  );
};

export default ProjectsPage;
