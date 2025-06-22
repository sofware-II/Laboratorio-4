import React from 'react';
import { useParams } from 'react-router-dom';

const TasksPage = () => {
  const { projectId } = useParams();

  return (
    <div className="container">
      <h2>Tasks for Project {projectId}</h2>
      <p>This is the Tasks page.</p>
    </div>
  );
};

export default TasksPage;
