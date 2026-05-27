import { render, screen, fireEvent } from '@testing-library/react';
import ProjectsList from './ProjectsList';

describe('ProjectsList - consulta de proyectos', () => {
  const projects = [
    { id: 1, name: 'Proyecto Alpha', description: 'Descripción Alpha' },
    { id: 2, name: 'Proyecto Beta', description: 'Descripción Beta' },
  ];

  const onSelect = jest.fn();
  const onEdit = jest.fn();
  const onDelete = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('debe renderizar la lista de proyectos consultados', () => {
    render(
      <ProjectsList
        projects={projects}
        onSelect={onSelect}
        onEdit={onEdit}
        onDelete={onDelete}
      />
    );

    expect(screen.getByText('Proyecto Alpha')).toBeInTheDocument();
    expect(screen.getByText('Proyecto Beta')).toBeInTheDocument();
    expect(screen.getByText('Descripción Alpha')).toBeInTheDocument();
  });

  test('debe mostrar mensaje cuando no hay proyectos', () => {
    render(
      <ProjectsList
        projects={[]}
        onSelect={onSelect}
        onEdit={onEdit}
        onDelete={onDelete}
      />
    );

    expect(screen.getByText('No projects found.')).toBeInTheDocument();
  });

  test('debe permitir seleccionar un proyecto para consultar sus tareas', () => {
    render(
      <ProjectsList
        projects={projects}
        onSelect={onSelect}
        onEdit={onEdit}
        onDelete={onDelete}
      />
    );

    fireEvent.click(screen.getByText('Proyecto Alpha'));

    expect(onSelect).toHaveBeenCalledWith(1);
  });

  test('debe invocar onEdit al mantener un proyecto', () => {
    render(
      <ProjectsList
        projects={projects}
        onSelect={onSelect}
        onEdit={onEdit}
        onDelete={onDelete}
      />
    );

    fireEvent.click(screen.getAllByTitle('Edit')[0]);

    expect(onEdit).toHaveBeenCalledWith(projects[0]);
  });

  test('debe invocar onDelete al eliminar un proyecto', () => {
    render(
      <ProjectsList
        projects={projects}
        onSelect={onSelect}
        onEdit={onEdit}
        onDelete={onDelete}
      />
    );

    fireEvent.click(screen.getAllByTitle('Delete')[0]);

    expect(onDelete).toHaveBeenCalledWith(projects[0]);
  });
});
