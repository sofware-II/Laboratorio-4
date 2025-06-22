import React, { useState, useEffect } from 'react';

const ProjectForm = ({ onSubmit, onCancel, initialData = null }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');

  useEffect(() => {
    if (initialData) {
      setName(initialData.name || '');
      setDescription(initialData.description || '');
    }
  }, [initialData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    onSubmit({ name: name.trim(), description: description.trim() });
    setName('');
    setDescription('');
  };

  return (
    <form onSubmit={handleSubmit} className="project-form">
      <h3>{initialData ? 'Edit Project' : 'Create Project'}</h3>
      <input
        type="text"
        placeholder="Project Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
        required
      />
      <textarea
        placeholder="Project Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        rows="3"
      />
      <div className="form-actions">
        <button type="submit">{initialData ? 'Update' : 'Create'}</button>
        {onCancel && (
          <button type="button" onClick={onCancel} className="btn-cancel">
            Cancel
          </button>
        )}
      </div>
    </form>
  );
};

export default ProjectForm;
