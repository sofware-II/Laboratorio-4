jest.mock('./api', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
    post: jest.fn(),
  },
}));

import api from './api';
import {
  fetchProjects,
  fetchProjectById,
  updateProject,
  deleteProject,
} from './projects';

describe('projects API - consulta y mantenimiento', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('fetchProjects debe consultar todos los proyectos', async () => {
    const mockResponse = { data: [{ id: 1, name: 'Proyecto 1' }] };
    api.get.mockResolvedValue(mockResponse);

    const result = await fetchProjects();

    expect(api.get).toHaveBeenCalledWith('/projects/all');
    expect(result).toEqual(mockResponse);
  });

  test('fetchProjectById debe consultar un proyecto por id', async () => {
    const mockResponse = { data: { id: 1, name: 'Proyecto 1' } };
    api.get.mockResolvedValue(mockResponse);

    const result = await fetchProjectById(1);

    expect(api.get).toHaveBeenCalledWith('/projects/1');
    expect(result).toEqual(mockResponse);
  });

  test('updateProject debe actualizar un proyecto existente', async () => {
    const payload = { name: 'Proyecto actualizado', description: 'Nueva descripción' };
    const mockResponse = { data: { id: 1, ...payload } };
    api.put.mockResolvedValue(mockResponse);

    const result = await updateProject(1, payload);

    expect(api.put).toHaveBeenCalledWith('/projects/update/1', payload);
    expect(result).toEqual(mockResponse);
  });

  test('deleteProject debe eliminar un proyecto', async () => {
    const mockResponse = { data: null };
    api.delete.mockResolvedValue(mockResponse);

    const result = await deleteProject(1);

    expect(api.delete).toHaveBeenCalledWith('/projects/delete/1');
    expect(result).toEqual(mockResponse);
  });
});
