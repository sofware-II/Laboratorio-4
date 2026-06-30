# Parte 2: Modelo de dominio general

## 1. Dominio del sistema

El proyecto Laboratorio-4 corresponde a un sistema de gestión de proyectos y tareas. Su dominio principal es permitir que el usuario administre proyectos, registre tareas asociadas a cada proyecto, consulte tareas por proyecto, actualice información y elimine registros cuando sea necesario.

Desde el enfoque Domain-Driven Design, el sistema representa un dominio de planificación y seguimiento de trabajo, donde los conceptos principales son Project y Task.

---

## 2. Entidades principales

| Entidad | Descripción | Atributos principales |
|---|---|---|
| Project | Representa un proyecto dentro del sistema. | id, name, description |
| Task | Representa una tarea asociada a un proyecto. | id, title, description, status, priority, project |

---

## 3. Objetos de valor / Enums

| Elemento | Descripción |
|---|---|
| TaskStatus | Define el estado de una tarea. |
| TaskPriority | Define la prioridad asignada a una tarea. |

Los enums permiten limitar los valores posibles de estado y prioridad, evitando que se registren valores inválidos dentro del dominio.

---

## 4. Relaciones entre entidades

La relación principal del modelo es:

Project 1 ---- * Task

Un proyecto puede tener varias tareas, pero cada tarea pertenece a un único proyecto.

---

## 5. Agregados identificados

### Agregado Project

| Elemento | Detalle |
|---|---|
| Aggregate Root | Project |
| Responsabilidad | Gestionar la información general de un proyecto. |
| Relación | Puede agrupar varias tareas asociadas. |
| Regla de negocio | Un proyecto debe tener nombre y descripción para identificarse dentro del sistema. |

### Agregado Task

| Elemento | Detalle |
|---|---|
| Aggregate Root | Task |
| Responsabilidad | Gestionar la información individual de una tarea. |
| Relación | Depende de un Project existente. |
| Regla de negocio | Una tarea debe pertenecer a un proyecto y debe tener estado y prioridad válidos. |

---

## 6. Servicios del dominio / aplicación

| Servicio | Responsabilidad |
|---|---|
| ProjectService | Crear, listar, buscar, actualizar y eliminar proyectos. |
| TaskService | Crear, listar por proyecto, buscar, actualizar y eliminar tareas. |

---

## 7. Repositorios

| Repositorio | Responsabilidad |
|---|---|
| ProjectRepository | Acceso a datos de Project. |
| TaskRepository | Acceso a datos de Task. |

---

## 8. Modelo de dominio general

```mermaid
classDiagram
    class Project {
        Long id
        String name
        String description
    }

    class Task {
        Long id
        String title
        String description
        TaskStatus status
        TaskPriority priority
        Project project
    }

    class TaskStatus {
        Enum values
    }

    class TaskPriority {
        Enum values
    }

    Project "1" --> "*" Task : contiene
    Task --> TaskStatus
    Task --> TaskPriority