# Proyecto Final - Task Manager

Este proyecto corresponde al desarrollo e integración de un sistema **Task Manager** con enfoque en CI/CD, DDD, pruebas automatizadas y validaciones de calidad.

---

## 👥 Integrante 1 - CI/CD con Jenkins y SonarQube
- **Análisis estático:** ejecutado con SonarScanner y cobertura integrada con JaCoCo.
- **Resultados:**
  - Cobertura inicial: `0.0%`
  - Cobertura final: `67.9%`
  - Cobertura requerida: `80.0%`
  - Quality Gate: **Failed** (por política de cobertura mínima).
- **Pipeline CI/CD:** configurado en Jenkins con etapas de:
  - Checkout
  - Verificación de herramientas
  - Pruebas backend
  - Build frontend
  - Análisis SonarQube
- **Observación:** etapas de Selenium, JMeter y Docker parametrizadas para integración final.

---

## 👥 Integrante 2 - DDD y Arquitectura
- **Dominio principal:** gestión de proyectos y tareas.
- **Entidades:** `Project`, `Task`
- **Objetos de valor:** `TaskStatus`, `TaskPriority`
- **Relación principal:** un proyecto contiene varias tareas; cada tarea pertenece a un único proyecto.
- **Módulos definidos:**
  - Project Management
  - Task Management
  - Shared Kernel
  - Frontend/API
- **Diagramas elaborados:**
  - `domain-class-diagram.png`
  - `modules-diagram.png`
  - `package-diagram.png`
  - `bounded-contexts.png`

---

## ⚙️ Backend, Swagger y Pruebas Unitarias
- **Tecnologías:** Java 21, Spring Boot 3.5.3, Spring Data JPA, PostgreSQL, H2, Flyway, Lombok, MapStruct, JUnit 5, Mockito, MockMvc.
- **Swagger UI:** disponible en  
  `http://localhost:8080/swagger-ui/index.html`
- **Endpoints principales:**
  - `POST /projects/add` → Crear proyecto
  - `GET /projects/all` → Listar proyectos
  - `GET /projects/{id}` → Obtener proyecto por ID
  - `PUT /projects/update/{id}` → Actualizar proyecto
  - `DELETE /projects/delete/{id}` → Eliminar proyecto
  - `POST /tasks/add` → Crear tarea
  - `GET /tasks/project/{id}` → Listar tareas de un proyecto
  - `GET /tasks/{id}` → Obtener tarea por ID
  - `PUT /tasks/update/{id}` → Actualizar tarea
  - `DELETE /tasks/delete/{id}` → Eliminar tarea
- **Pruebas unitarias:**
  - 42 pruebas ejecutadas
  - 0 fallos, 0 errores, 1 omitida
  - **BUILD SUCCESS**

---

## 👥 Integrante 4 - Pruebas Funcionales con Selenium
- **Objetivo:** validar operaciones CRUD de proyectos desde la interfaz gráfica.
- **Escenarios validados:**
  - Creación, visualización, actualización y eliminación de proyectos
  - Navegación entre vistas
  - Sincronización frontend-backend
- **Resultados:**
  - Todas las pruebas ejecutadas correctamente
  - Persistencia confirmada en PostgreSQL
  - Evidencias visuales generadas
- **Observación:** algunos timeouts en elementos dinámicos, pero ejecución válida.

---

## 👥 Integrante 6 - Pruebas de Rendimiento y Seguridad
- **Apache JMeter:**
  - Evaluación de endpoints REST (GET, POST, PUT, DELETE)
  - Métricas: tiempo de respuesta, porcentaje de errores, APDEX, throughput
  - Integración con Jenkins para ejecución automática
- **OWASP ZAP:**
  - Escaneo de seguridad sobre la aplicación web
  - Vulnerabilidad detectada: **Application Error Disclosure** (riesgo bajo)
  - Oportunidad de mejora en manejo de excepciones

---

## 📂 Evidencias
- `docs/jenkins/` → Pipeline CI/CD
- `docs/diagrams/` → Diagramas DDD
- `docs/testing/evidencias/` → Swagger, pruebas unitarias y funcionales
- `docs/integrante-2-ddd/evidencias/` → Evidencias DDD
- Reportes JMeter y OWASP ZAP en HTML

---

## ✅ Conclusión
El proyecto Task Manager integra:
- CI/CD con Jenkins y SonarQube
- Arquitectura basada en DDD
- Backend robusto con Spring Boot
- Documentación automática con Swagger
- Pruebas unitarias, funcionales, de rendimiento y seguridad

Este trabajo asegura una base sólida para futuras mejoras, incluyendo integración de Selenium, JMeter y Docker en el pipeline final.
