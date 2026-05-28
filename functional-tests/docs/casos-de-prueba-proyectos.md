# Casos de prueba — Consulta y mantenimiento de proyectos

Módulo: **Integrante 5** — Task Manager (Lab 05)

## Prerrequisitos

- Backend Spring Boot en `http://localhost:8080`
- Frontend React en `http://localhost:3000`
- PostgreSQL disponible
- Google Chrome instalado

## Casos automatizados

| ID | Caso | Método | Resultado esperado |
|----|------|--------|-------------------|
| CP-01 | Listado de proyectos | `shouldDisplayProjectsListPage` | Título "Projects", tabla con columnas ID, Name, Description, Actions |
| CP-02 | Consulta de tareas por proyecto | `shouldNavigateToProjectTasksFromList` | Navegación a `/projects/{id}/tasks` y título "Project Tasks" |
| CP-03 | Abrir formulario de edición | `shouldOpenEditFormForProject` | Formulario "Edit Project" con nombre precargado |
| CP-04 | Actualizar proyecto | `shouldUpdateProjectFromList` | Nombre y descripción actualizados en la tabla |
| CP-05 | Eliminar proyecto | `shouldDeleteProjectAfterConfirmation` | Proyecto desaparece tras confirmar eliminación |
| CP-06 | Cancelar eliminación | `shouldCancelProjectDeletion` | Proyecto permanece visible tras cancelar |

## Ejecución

```bash
export JAVA_HOME="/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home"
cd functional-tests
mvn test -Dtest=ProjectMaintenanceFunctionalTest
```

Con navegador visible:

```bash
mvn test -Dtest=ProjectMaintenanceFunctionalTest -Dselenium.headless=false
```

## Notas

- Cada test crea proyectos con nombres únicos (`UUID`) para evitar colisiones.
- Los tests de mantenimiento (CP-03 a CP-06) crean datos vía UI antes de ejecutar la acción.
- Requiere la infraestructura Selenium base (`feature/lab-05-selenium-setup`) mergeada o incluida en esta rama.
