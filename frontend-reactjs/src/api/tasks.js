import api from './api';

export const fetchTasksByProject = (projectId) => api.get(`/tasks/project/${projectId}`);
export const createTask = (data) => api.post(`/tasks/add`, data);
export const updateTask = (id, data) => api.put(`/tasks/update/${id}`, data);
export const deleteTask = (id) => api.delete(`/tasks/delete/${id}`);
export const fetchTaskById = (id) => api.get(`/tasks/${id}`);
