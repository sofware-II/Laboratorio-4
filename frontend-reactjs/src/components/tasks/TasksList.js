import React from 'react';
import { FiEdit, FiTrash2 } from 'react-icons/fi';

const TasksList = ({ tasks, onEdit, onDelete }) => {
  if (!tasks || tasks.length === 0) {
    return <p>No tasks found.</p>;
  }

  return (
    <table className="task-table">
      <thead>
        <tr>
          <th>Title</th>
          <th>Status</th>
          <th>Priority</th>
          <th>Actions</th>
        </tr>
      </thead>
      <tbody>
        {tasks.map(task => (
          <tr key={task.id}>
            <td>{task.title}</td>
            <td>{task.status}</td>
            <td>{task.priority}</td>
            <td>
              <button
                title="Edit Task"
                onClick={() => onEdit(task)}
                className="icon-btn"
              >
                <FiEdit size={16} color="#2563eb" />
              </button>
              <button
                title="Delete Task"
                onClick={() => onDelete(task)}
                className="icon-btn"
              >
                <FiTrash2 size={16} color="#dc2626" />
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default TasksList;
