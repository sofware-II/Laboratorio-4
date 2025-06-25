import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchProjectById } from '../api/projects';
import { fetchTasksByProject, createTask, updateTask, deleteTask } from '../api/tasks';
import TasksList from '../components/tasks/TasksList';
import TaskForm from '../components/tasks/TaskForm';
import ConfirmModal from '../components/ConfirmModal';


const TasksPage = () => {
  const { projectId } = useParams();
  const [project, setProject] = useState(null);
  const [tasks, setTasks] = useState([]);
  const [formOpen, setFormOpen] = useState(false);
  const [editingTask, setEditingTask] = useState(null);
  const [taskModalOpen, setTaskModalOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState(null);



  useEffect(() => {
    fetchProjectById(projectId)
      .then(res => setProject(res.data))
      .catch(console.error);

    fetchTasksByProject(projectId)
      .then(res => setTasks(res.data))
      .catch(console.error);
  }, [projectId]);

  const loadTasks = () => {
    fetchTasksByProject(projectId)
      .then(res => setTasks(res.data))
      .catch(console.error);
  };

  const handleCreateClick = () => {
    setEditingTask(null);
    setFormOpen(true);
  };

  const handleEditTask = (task) => {
    setEditingTask(task);
    setFormOpen(true);
  };

  const handleFormSubmit = (taskData) => {
    if (editingTask) {
      updateTask(editingTask.id, taskData)
        .then(() => {
          loadTasks();
          setFormOpen(false);
          setEditingTask(null);
        })
        .catch(console.error);
    } else {
      createTask({ ...taskData, projectId: Number(projectId) })
        .then(() => {
          loadTasks();
          setFormOpen(false);
        })
        .catch(console.error);
    }
  };

  const handleFormCancel = () => {
    setFormOpen(false);
    setEditingTask(null);
  };

  const handleDeleteTask = (task) => {
    setSelectedTask(task);
    setTaskModalOpen(true);
  };

  const confirmTaskDelete = () => {
    if (!selectedTask) return;
    deleteTask(selectedTask.id)
      .then(() => {
        loadTasks();
        setTaskModalOpen(false);
        setSelectedTask(null);
      })
      .catch(console.error);
  };

  const cancelTaskDelete = () => {
    setTaskModalOpen(false);
    setSelectedTask(null);
  };

  return (
    <div className='Layout_container'>
      <h2>Project Tasks</h2>
      <div className="container">
        {project && (
          <div style={{ marginBottom: '1rem' }}>
            <h3>{project.name}</h3>
            <p>{project.description}</p>
          </div>
        )}

        {!formOpen && (
          <button onClick={handleCreateClick} style={{ marginBottom: '1rem' }}>
            + Add Task
          </button>
        )}

        {formOpen && (
          <TaskForm
            initialData={editingTask}
            onSubmit={handleFormSubmit}
            onCancel={handleFormCancel}
            projectId={projectId}
          />
        )}

        {!formOpen && (
          <TasksList
            tasks={tasks}
            onEdit={handleEditTask}
            onDelete={handleDeleteTask}
          />
        )}

        <ConfirmModal
          show={taskModalOpen}
          title="Confirm Task Deletion"
          message={`Are you sure you want to delete task "${selectedTask?.title}"?`}
          onConfirm={confirmTaskDelete}
          onCancel={cancelTaskDelete}
        />

      </div>
    </div>
  );
};

export default TasksPage;
