import { render, screen, fireEvent, waitFor, within } from '@testing-library/react';
import ProjectsPage from './ProjectsPage';
import * as projectsApi from '../api/projects';

const mockNavigate = jest.fn();

jest.mock('react-router-dom', () => ({
  MemoryRouter: ({ children }) => children,
  useNavigate: () => mockNavigate,
}));

jest.mock('../api/projects');

const projects = [
  { id: 1, name: 'Proyecto Alpha', description: 'Descripción Alpha' },
  { id: 2, name: 'Proyecto Beta', description: 'Descripción Beta' },
];

const renderProjectsPage = () => render(<ProjectsPage />);

describe('ProjectsPage - consulta y mantenimiento de proyectos', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    projectsApi.fetchProjects.mockResolvedValue({ data: projects });
    projectsApi.updateProject.mockResolvedValue({ data: projects[0] });
    projectsApi.deleteProject.mockResolvedValue({});
  });

  test('debe consultar y mostrar los proyectos al cargar la página', async () => {
    renderProjectsPage();

    await waitFor(() => {
      expect(projectsApi.fetchProjects).toHaveBeenCalledTimes(1);
    });

    expect(await screen.findByText('Proyecto Alpha')).toBeInTheDocument();
    expect(screen.getByText('Proyecto Beta')).toBeInTheDocument();
  });

  test('debe navegar al detalle de tareas al seleccionar un proyecto', async () => {
    renderProjectsPage();

    await screen.findByText('Proyecto Alpha');
    fireEvent.click(screen.getByText('Proyecto Alpha'));

    expect(mockNavigate).toHaveBeenCalledWith('/projects/1/tasks');
  });

  test('debe abrir el formulario de edición para mantener un proyecto', async () => {
    renderProjectsPage();

    await screen.findByText('Proyecto Alpha');
    fireEvent.click(screen.getAllByTitle('Edit')[0]);

    expect(screen.getByText('Edit Project')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Proyecto Alpha')).toBeInTheDocument();
  });

  test('debe actualizar un proyecto y recargar la consulta', async () => {
    renderProjectsPage();

    await screen.findByText('Proyecto Alpha');
    fireEvent.click(screen.getAllByTitle('Edit')[0]);

    fireEvent.change(screen.getByPlaceholderText('Project Name'), {
      target: { value: 'Proyecto Alpha actualizado' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Update' }));

    await waitFor(() => {
      expect(projectsApi.updateProject).toHaveBeenCalledWith(1, {
        name: 'Proyecto Alpha actualizado',
        description: 'Descripción Alpha',
      });
    });

    expect(projectsApi.fetchProjects).toHaveBeenCalledTimes(2);
  });

  test('debe confirmar y eliminar un proyecto', async () => {
    renderProjectsPage();

    await screen.findByText('Proyecto Alpha');
    fireEvent.click(screen.getAllByTitle('Delete')[0]);

    expect(screen.getByText('Confirm Deletion')).toBeInTheDocument();
    expect(
      screen.getByText('Are you sure you want to delete project "Proyecto Alpha"?')
    ).toBeInTheDocument();

    const modal = screen.getByText('Confirm Deletion').closest('.modal-box');
    fireEvent.click(within(modal).getByRole('button', { name: 'Delete' }));

    await waitFor(() => {
      expect(projectsApi.deleteProject).toHaveBeenCalledWith(1);
    });

    expect(projectsApi.fetchProjects).toHaveBeenCalledTimes(2);
  });

  test('debe cancelar la eliminación de un proyecto', async () => {
    renderProjectsPage();

    await screen.findByText('Proyecto Alpha');
    fireEvent.click(screen.getAllByTitle('Delete')[0]);
    const modal = screen.getByText('Confirm Deletion').closest('.modal-box');
    fireEvent.click(within(modal).getByRole('button', { name: 'Cancel' }));

    expect(projectsApi.deleteProject).not.toHaveBeenCalled();
    expect(screen.queryByText('Confirm Deletion')).not.toBeInTheDocument();
  });
});
