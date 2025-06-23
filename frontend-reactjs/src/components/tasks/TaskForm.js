import React, { useState, useEffect } from 'react';

const statusOptions = ['TODO', 'IN_PROGRESS', 'DONE'];
const priorityOptions = ['LOW', 'MEDIUM', 'HIGH'];

const TaskForm = ({ onSubmit, onCancel, initialData = null, projectId }) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('TODO');
  const [priority, setPriority] = useState('LOW');

  useEffect(() => {
    if (initialData) {
      setTitle(initialData.title || '');
      setDescription(initialData.description || '');
      setStatus(initialData.status || 'TODO');
      setPriority(initialData.priority || 'LOW');
    }
  }, [initialData]);

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!title.trim()) return;

    const task = {
      title: title.trim(),
      description: description.trim(),
      status,
      priority,
      projectId,
    };

    onSubmit(task);
    setTitle('');
    setDescription('');
    setStatus('TODO');
    setPriority('LOW');
  };

  return (
    <form className="task-form" onSubmit={handleSubmit}>
      <h3>{initialData ? 'Edit Task' : 'Create Task'}</h3>
      <input
        type="text"
        placeholder="Task Title"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        required
      />
      <textarea
        placeholder="Task Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        rows="3"
      />
      <div className="select-group">
        <select value={status} onChange={(e) => setStatus(e.target.value)}>
          {statusOptions.map((s) => (
            <option key={s} value={s}>{s}</option>
          ))}
        </select>
        <select value={priority} onChange={(e) => setPriority(e.target.value)}>
          {priorityOptions.map((p) => (
            <option key={p} value={p}>{p}</option>
          ))}
        </select>
      </div>
      <div className="form-actions">
        <button type="submit">{initialData ? 'Update' : 'Create'}</button>
        <button type="button" className="btn-cancel" onClick={onCancel}>Cancel</button>
      </div>
    </form>
  );
};

export default TaskForm;
