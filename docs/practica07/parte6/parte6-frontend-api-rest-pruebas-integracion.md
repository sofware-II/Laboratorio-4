# Parte 6 - Frontend, API REST, Pruebas e Integración

## Objetivo
El objetivo es implementar y evidenciar el desacoplamiento entre el frontend y el backend mediante una API REST, validando los endpoints, asegurando la calidad del código en SonarQube y preparando las evidencias funcionales.

## Relación con DDD y Bounded Contexts
El sistema se divide en **Project Management** y **Task Management**. El frontend se comunica de forma completamente desacoplada con el backend utilizando DTOs para la transferencia de información, respetando así la encapsulación de las entidades de dominio en el backend. Las solicitudes del frontend se rigen por la estructura de la API REST que funciona como la capa de presentación/controladores de cada Bounded Context.

## Arquitectura de Integración
- **Backend (Spring Boot)**: Expone endpoints RESTful bajo `/projects` y `/tasks`. El acceso Cross-Origin (CORS) está habilitado explícitamente para el frontend (`http://localhost:3000`).
- **Frontend (React)**: Cuenta con una capa de API (`api.js`, `projects.js`, `tasks.js`) configurada para usar una variable de entorno `REACT_APP_API_BASE_URL` para realizar peticiones mediante Axios.

## Endpoints Validados
### Project Management
- `GET /projects/all`: Lista todos los proyectos.
- `GET /projects/{id}`: Detalle de un proyecto.
- `POST /projects/add`: Crea un proyecto.
- `PUT /projects/update/{id}`: Actualiza un proyecto.
- `DELETE /projects/delete/{id}`: Elimina un proyecto.

### Task Management
- `GET /tasks/project/{projectId}`: Lista tareas por proyecto.
- `GET /tasks/{id}`: Detalle de una tarea.
- `POST /tasks/add`: Crea una tarea para un proyecto.
- `PUT /tasks/update/{id}`: Actualiza una tarea.
- `DELETE /tasks/delete/{id}`: Elimina una tarea.

## Archivos Modificados Principales
- `frontend-reactjs/.env`: Creación para la base URL.
- `frontend-reactjs/src/api/api.js`: Uso de variables de entorno.
- `frontend-reactjs/src/components/*`: Múltiples componentes (`ConfirmModal`, `ProjectForm`, `ProjectsList`, `TaskForm`, `TasksList`) refactorizados para usar `PropTypes` y protecciones sobre listas nulas (SonarQube fixes).
- `frontend-reactjs/src/styles.css`: Solución a la advertencia de contraste visual en los botones.
- `docs/practica07/parte6/*`: Documentación y recursos generados.

## Flujo Frontend-Backend
El usuario interactúa con los componentes visuales de React. Las acciones disparan llamadas asincrónicas mediante `axios` (en la capa de servicios API) hacia el servidor Spring Boot en el puerto `8080`. El servidor procesa la lógica y responde con DTOs en formato JSON, los cuales son interpretados por el frontend para actualizar los estados y re-renderizar las vistas.

## Pruebas Ejecutadas
- `mvn clean test` / `mvn clean install` en backend: Validación de unit tests usando MockMvc y generación de reporte JaCoCo.
- `npm test -- --watchAll=false` en frontend: Pruebas de renderizado y simulación en componentes.
- `npm run build` en frontend: Compilación exitosa para producción.

## Comandos Usados
- `mvn clean test`
- `mvn clean install`
- `npm install prop-types`
- `npm test -- --watchAll=false`
- `npm run build`

## Conclusiones
Se ha logrado con éxito el desacoplamiento. El código frontend ahora es más seguro frente a props inválidas o arrays indefinidos, cumpliendo con los estándares de SonarQube, y la integración a través de CORS permite una interacción robusta y en tiempo real con la base de datos de PostgreSQL sin el uso de mocks o datos quemados.
