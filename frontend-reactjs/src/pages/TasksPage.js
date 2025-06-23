import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchProjectById } from '../api/projects';
import { fetchTasksByProject } from '../api/tasks';
import TasksList from '../components/tasks/TasksList';

const TasksPage = () => {
  const { projectId } = useParams();
  const [project, setProject] = useState(null);
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    fetchProjectById(projectId)
      .then(res => setProject(res.data))
      .catch(console.error);

    fetchTasksByProject(projectId)
      .then(res => setTasks(res.data))
      .catch(console.error);
  }, [projectId]);

  return (
    <div>
      <h2>Project Tasks</h2>
      <div className="container">
        {project && (
          <div style={{ marginBottom: '1rem' }}>
            <h3>{project.name}</h3>
            <p>{project.description}</p>
          </div>
        )}

        <TasksList
          tasks={tasks}
          onEdit={(task) => console.log('Edit', task)}
          onDelete={(task) => console.log('Delete', task)}
        />

      </div>
    </div>
  );
};

export default TasksPage;
