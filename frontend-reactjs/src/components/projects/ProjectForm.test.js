import { render, screen, fireEvent } from '@testing-library/react';
import ProjectForm from './ProjectForm';

describe('ProjectForm - mantenimiento de proyectos', () => {
  const onSubmit = jest.fn();
  const onCancel = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('debe cargar datos iniciales al editar un proyecto', () => {
    render(
      <ProjectForm
        initialData={{ id: 1, name: 'Proyecto existente', description: 'Descripción existente' }}
        onSubmit={onSubmit}
        onCancel={onCancel}
      />
    );

    expect(screen.getByDisplayValue('Proyecto existente')).toBeInTheDocument();
    expect(screen.getByDisplayValue('Descripción existente')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Update' })).toBeInTheDocument();
  });

  test('debe enviar los datos actualizados al mantener un proyecto', () => {
    render(
      <ProjectForm
        initialData={{ id: 1, name: 'Proyecto existente', description: 'Descripción existente' }}
        onSubmit={onSubmit}
        onCancel={onCancel}
      />
    );

    fireEvent.change(screen.getByPlaceholderText('Project Name'), {
      target: { value: 'Proyecto actualizado' },
    });
    fireEvent.change(screen.getByPlaceholderText('Project Description'), {
      target: { value: 'Descripción actualizada' },
    });
    fireEvent.click(screen.getByRole('button', { name: 'Update' }));

    expect(onSubmit).toHaveBeenCalledWith({
      name: 'Proyecto actualizado',
      description: 'Descripción actualizada',
    });
  });

  test('debe cancelar la edición de un proyecto', () => {
    render(
      <ProjectForm
        initialData={{ id: 1, name: 'Proyecto existente', description: 'Descripción existente' }}
        onSubmit={onSubmit}
        onCancel={onCancel}
      />
    );

    fireEvent.click(screen.getByRole('button', { name: 'Cancel' }));

    expect(onCancel).toHaveBeenCalled();
    expect(onSubmit).not.toHaveBeenCalled();
  });
});
