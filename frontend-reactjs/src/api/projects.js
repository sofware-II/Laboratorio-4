import api from './api';

// Get all projects
export const fetchProjects = () => api.get('/projects/all');

// Get a project by ID
export const fetchProjectById = (id) => api.get(`/projects/${id}`);

// Create a new project
export const createProject = (data) => api.post('/projects/add', data);

// Update a project by ID
export const updateProject = (id, data) => api.put(`/projects/update/${id}`, data);

// Delete a project by ID
export const deleteProject = (id) => api.delete(`/projects/delete/${id}`);
