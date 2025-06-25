import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ProjectsList from '../components/projects/ProjectsList';
import ConfirmModal from '../components/ConfirmModal';
import ProjectForm from '../components/projects/ProjectForm';
import { fetchProjects, deleteProject, createProject, updateProject } from '../api/projects';

const ProjectsPage = () => {
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [modalOpen, setModalOpen] = useState(false);
  const [selectedProject, setSelectedProject] = useState(null);
  const [formOpen, setFormOpen] = useState(false);
  const [editingProject, setEditingProject] = useState(null);


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

  const handleCreateClick = () => {
    setEditingProject(null);
    setFormOpen(true);
  };

  const handleEditClick = (project) => {
    setEditingProject(project);
    setFormOpen(true);
  };

  const handleFormSubmit = (formData) => {
    if (editingProject) {
      updateProject(editingProject.id, formData)
        .then(() => {
          loadProjects();
          setFormOpen(false);
          setEditingProject(null);
        })
        .catch(console.error);
    } else {
      createProject(formData)
        .then(() => {
          loadProjects();
          setFormOpen(false);
        })
        .catch(console.error);
    }
  };

  const handleFormCancel = () => {
    setFormOpen(false);
    setEditingProject(null);
  };


  return (
    <div className='Layout_container'>
      <h2>Projects</h2>
      <div className="container">
        {!formOpen && (
          <button onClick={handleCreateClick} style={{ marginBottom: '1rem' }}>
            + Add Project
          </button>
        )}
          
        {formOpen ? (
          <ProjectForm
            initialData={editingProject}
            onSubmit={handleFormSubmit}
            onCancel={handleFormCancel}
          />
        ) : (
          <ProjectsList
            projects={projects}
            onSelect={handleProjectSelect}
            onEdit={handleEditClick}
            onDelete={handleDeleteClick}
          />
        )}
        
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
