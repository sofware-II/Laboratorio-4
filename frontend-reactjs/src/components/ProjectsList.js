import React, { useEffect, useState } from 'react';
import { fetchProjects } from '../api/projects';
import { FiEdit, FiTrash2 } from 'react-icons/fi'; // Feather icons

const ProjectsList = ({ onSelect, onEdit, onDelete }) => {
  const [projects, setProjects] = useState([]);

  useEffect(() => {
    fetchProjects()
      .then((res) => setProjects(res.data))
      .catch(console.error);
  }, []);

  return (
    <table style={{ width: '100%', borderCollapse: 'collapse' }}>
      <thead>
        <tr style={{ backgroundColor: '#eee' }}>
          <th style={{ border: '1px solid #ccc', padding: '8px' }}>ID</th>
          <th style={{ border: '1px solid #ccc', padding: '8px' }}>Name</th>
          <th style={{ border: '1px solid #ccc', padding: '8px' }}>Description</th>
          <th style={{ border: '1px solid #ccc', padding: '8px' }}>Actions</th>
        </tr>
      </thead>
      <tbody>
        {projects.length === 0 ? (
          <tr>
            <td colSpan="4" style={{ padding: '8px', textAlign: 'center' }}>
              No projects found.
            </td>
          </tr>
        ) : (
          projects.map((project) => (
            <tr key={project.id} style={{ border: '1px solid #ccc' }}>
              <td
                data-label="ID"
                style={{ cursor: 'pointer' }}
                onClick={() => onSelect(project.id)}
              >
                {project.id}
              </td>
              <td
                data-label="Name"
                style={{ cursor: 'pointer' }}
                onClick={() => onSelect(project.id)}
              >
                {project.name}
              </td>
              <td data-label="Description">{project.description || '-'}</td>
              <td data-label="Actions">
                <button
                  onClick={() => onEdit(project)}
                  style={{ marginRight: '8px', background: 'none', border: 'none', cursor: 'pointer' }}
                  title="Edit Project"
                >
                  <FiEdit size={18} color="#2563eb" />
                </button>
                <button
                  onClick={() => {
                    if (
                      window.confirm(`Are you sure you want to delete project "${project.name}"?`)
                    ) {
                      onDelete(project.id);
                    }
                  }}
                  style={{ background: 'none', border: 'none', cursor: 'pointer' }}
                  title="Delete Project"
                >
                  <FiTrash2 size={18} color="#dc2626" />
                </button>
              </td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  );
};

export default ProjectsList;
