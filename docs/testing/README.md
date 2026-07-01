# 🧪 Validación y Documentación del Backend (Punto 3)

Documentación técnica, pruebas unitarias y verificación de endpoints del backend (`backend-springboot`) para el Proyecto Final.

---

## ⚡ 1. Stack Tecnológico
* **Core:** Java 21 + Spring Boot 3.5.3 (Web, Data JPA, Validation).
* **Persistencia:** PostgreSQL + Hibernate ORM (producción/desarrollo) | **H2 en memoria** (pruebas).
* **Herramientas:** **MapStruct 1.6.3** (mapeo DTOs), **Lombok** e inyección por `@RequiredArgsConstructor`.
* **Calidad y BD:** **Flyway** (migraciones en `db/migration/`), **JUnit 5**, **Mockito** y **JaCoCo**.

---

## 🚀 2. Guía Rápida de Comandos

Abre tu terminal en la carpeta `backend-springboot/` y ejecuta:

| Acción | Comando (PowerShell) |
| :--- | :--- |
| **Correr las 42 pruebas unitarias** | `.\mvnw.cmd test` |
| **Compilar, validar y empaquetar (.jar)** | `.\mvnw.cmd clean install` |
| **Levantar servidor local** | `.\mvnw.cmd spring-boot:run` |

---

## 🌐 3. Endpoints y Swagger UI

Con el servidor encendido (`.\mvnw.cmd spring-boot:run`), accede localmente a:
* 🟢 **Swagger UI (Interactiva):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* 📜 **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Resumen de Rutas REST
* **Proyectos (`/projects`):** `POST /add` | `GET /all` | `GET /{id}` | `PUT /update/{id}` | `DELETE /delete/{id}`
* **Tareas (`/tasks`):** `POST /add` | `GET /project/{id}` | `GET /{id}` | `PUT /update/{id}` | `DELETE /delete/{id}`

---

## 📁 4. Índice de Documentación y Evidencias

| Archivo / Carpeta | Descripción Puntual |
| :--- | :--- |
| [`resumen-backend.md`](./resumen-backend.md) | Arquitectura del sistema (Capas vs DDD), DTOs y relación con BD. |
| [`swagger-endpoints.md`](./swagger-endpoints.md) | Catálogo detallado de parámetros y respuestas REST + OpenAPI. |
| [`pruebas-unitarias.md`](./pruebas-unitarias.md) | Análisis de las 42 pruebas (Servicios, Controladores, JPA) y cobertura. |
| [`comandos-ejecucion.md`](./comandos-ejecucion.md) | Manual paso a paso de comandos Git y Maven para el backend. |
| [`resumen-google-docs.md`](./resumen-google-docs.md) | Párrafo ejecutivo sintético listo para pegar en Google Docs. |
| [`evidencias/`](./evidencias/) | Capturas PNG: [`mvn-test.png`](./evidencias/mvn-test.png), [`mvn-clean-install.png`](./evidencias/mvn-clean-install.png) y [`swagger-ui.png`](./evidencias/swagger-ui.png). |
