import React, { useEffect, useState } from 'react';
import { fetchProjects } from '../../api/projects';
import { FiEdit, FiTrash2, FiEye} from 'react-icons/fi';

const ProjectsList = ({ projects, onSelect, onEdit, onDelete }) => {
  
  return (
    <table className="project-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Name</th>
          <th>Description</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {projects.length === 0 ? (
          <tr>
            <td colSpan="4" style={{ textAlign: 'center' }}>
              No projects found.
            </td>
          </tr>
        ) : (
          projects.map((project) => (
            <tr key={project.id}>
              <td data-label="ID" onClick={() => onSelect(project.id)} style={{ cursor: 'pointer' }}>
                {project.id}
              </td>
              <td data-label="Name" onClick={() => onSelect(project.id)} style={{ cursor: 'pointer' }}>
                {project.name}
              </td>
              <td data-label="Description">{project.description || '-'}</td>
              <td data-label="Actions">
                <button
                  title="Edit"
                  onClick={() => onEdit(project)}
                  className="icon-btn"
                >
                  <FiEdit size={18} color="#2563eb" />
                </button>
                <button
                  title="View Tasks"
                  onClick={() => onSelect(project.id)}
                  className="icon-btn"
                >
                  <FiEye size={18} />
                </button>
                <button
                  title="Delete"
                  onClick={() => onDelete(project)}
                  className="icon-btn"
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
