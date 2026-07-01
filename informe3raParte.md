# Informe Técnico – Punto 3: Backend, Swagger, Endpoints y Pruebas Unitarias

**Proyecto Final:** Sistema de Gestión de Tareas (`taskManager`)  
**Rama de Trabajo:** `docs/testing`  
**Módulo Evaluado:** `backend-springboot`

---

## 1. Resumen y Pila Tecnológica

El backend centraliza las reglas de negocio, valida las peticiones HTTP externas y gestiona la persistencia de dos recursos principales: **Proyectos (`Project`)** y **Tareas (`Task`)**.

- **Lenguaje y Framework:** Java 21 y Spring Boot 3.5.3 (Web, Data JPA, Validation).
- **Base de Datos:** PostgreSQL en desarrollo/producción con ORM Hibernate, y **H2 Database en memoria** para la ejecución veloz y aislada de pruebas automatizadas.
- **Migraciones:** Flyway (`src/main/resources/db/migration/`) para versionar y garantizar la coherencia del esquema de la base de datos.
- **Herramientas:** **Lombok** para reducir código repetitivo y **MapStruct 1.6.3** para el mapeo automatizado en tiempo de compilación entre Entidades JPA y DTOs.

---

## 2. Arquitectura del Sistema

El proyecto combina dos estilos arquitectónicos limpios y modulares según el subdominio:

1. **Módulo de Tareas (Arquitectura en Capas Tradicional):** Estructurado en paquetes funcionales (`controller`, `service`, `repository`, `entity`, `dto`, `mapper`). Utiliza `TaskValidator` para validar de manera centralizada enumeraciones como `TaskStatus` (`TODO`, `IN_PROGRESS`, `DONE`) y `TaskPriority` (`LOW`, `MEDIUM`, `HIGH`).
2. **Módulo de Proyectos (Diseño Orientado al Dominio - DDD):** Organizado en subpaquetes de responsabilidad (`project.presentation`, `project.application`, `project.domain`, `project.infrastructure`), logrando una mayor separación de incumbencias.

---

## 3. Documentación de Endpoints y Swagger UI

La API REST implementa la especificación **OpenAPI 3.0** mediante `springdoc-openapi-starter-webmvc-ui`. Con el servidor activo (`.\mvnw.cmd spring-boot:run`), la documentación se consulta en:

- **Swagger UI interactivo:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON en crudo:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Catálogo de Endpoints Principales

Todas las rutas base provienen de constantes centralizadas en la clase `ApiPaths`:

|  Método  | Ruta REST               | Descripción Breve                                   | Respuesta Esperada            |
| :------: | :---------------------- | :-------------------------------------------------- | :---------------------------- |
|  `POST`  | `/projects/add`         | Crea un proyecto en la base de datos                | `200 OK` + `ProjectDto`       |
|  `GET`   | `/projects/all`         | Retorna todos los proyectos registrados             | `200 OK` + `List<ProjectDto>` |
|  `GET`   | `/projects/{id}`        | Busca un proyecto por su identificador numérico     | `200 OK` o `404 Not Found`    |
|  `PUT`   | `/projects/update/{id}` | Actualiza el nombre o descripción del proyecto      | `200 OK` + `ProjectDto`       |
| `DELETE` | `/projects/delete/{id}` | Elimina el registro del proyecto en base de datos   | `204 No Content`              |
|  `POST`  | `/tasks/add`            | Crea una tarea vinculada a un `projectId` existente | `200 OK` o `404 Not Found`    |
|  `GET`   | `/tasks/project/{id}`   | Lista todas las tareas asociadas a un proyecto      | `200 OK` + `List<TaskDto>`    |
|  `GET`   | `/tasks/{id}`           | Consulta una tarea individual por su ID             | `200 OK` o `404 Not Found`    |
|  `PUT`   | `/tasks/update/{id}`    | Modifica estado, prioridad y datos de la tarea      | `200 OK` o `400 Bad Request`  |
| `DELETE` | `/tasks/delete/{id}`    | Remueve una tarea de forma definitiva               | `204 No Content`              |

### Evidencia de Swagger UI

A continuación se ilustra la interfaz interactiva con los endpoints en funcionamiento:

![Swagger UI - Endpoints del Backend](docs/testing/evidencias/swagger-ui.png)

_Referencia:_ `docs/testing/evidencias/swagger-ui.png`

---

## 4. Pruebas Unitarias e Integración

La calidad del código se garantiza mediante una suite de **42 pruebas automatizadas** desarrolladas con **JUnit 5**, **Mockito** y **MockMvc**:

- **Servicios (`TaskServiceTest`, `ProjectServiceTest`):** Validan la lógica de negocio, mapeos DTO, operaciones CRUD y que se lancen excepciones correctas (`ResourceNotFoundException`, `IllegalArgumentException`) ante entradas inválidas.
- **Controladores (`TaskControllerTest`, `ProjectControllerTest`):** Simulan peticiones HTTP (`GET`, `POST`, `PUT`, `DELETE`) sin tocar la base de datos, verificando códigos HTTP y payloads JSON con `jsonPath`.
- **Repositorios y Dominio (`TaskRepositoryTest`, `ProjectRepositoryTest`):** Verifican consultas personalizadas (ej. `findByProjectId`) e integridad referencial sobre la base de datos en memoria **H2**.

### Validación y Evidencias Maven

#### 1. Ejecución de Pruebas (`mvn test`)

Ejecuta la suite de pruebas y calcula la cobertura de código en las 17 clases del sistema mediante **JaCoCo**.

```powershell
cd backend-springboot
.\mvnw.cmd test
```

**Resultado obtenido:** 42 pruebas ejecutadas (0 fallos, 0 errores, 1 skipped) y confirmación de `BUILD SUCCESS`.

![Evidencia mvn test](docs/testing/evidencias/mvn-test.png)

_Referencia:_ `docs/testing/evidencias/mvn-test.png`

#### 2. Validación y Empaquetado (`mvn clean install`)

Limpia artefactos previos, autogenera implementaciones de MapStruct/Lombok, pasa todos los tests y construye el ejecutable `taskManager-0.0.1-SNAPSHOT.jar`.

```powershell
.\mvnw.cmd clean install
```

**Resultado obtenido:** Empaquetado exitoso e instalación en repositorio local con status `BUILD SUCCESS`.

![Evidencia mvn clean install](docs/testing/evidencias/mvn-clean-install.png)

_Referencia:_ `docs/testing/evidencias/mvn-clean-install.png`

---

## 5. Resumen de Comandos Rápidos

| Acción                                     | Comando en Terminal (PowerShell) |
| :----------------------------------------- | :------------------------------- |
| **Cambiar a rama de documentación**        | `git checkout -b docs/testing`   |
| **Entrar al directorio del backend**       | `cd backend-springboot`          |
| **Ejecutar pruebas y JaCoCo**              | `.\mvnw.cmd test`                |
| **Validar y compilar el proyecto (.jar)**  | `.\mvnw.cmd clean install`       |
| **Levantar servidor local en puerto 8080** | `.\mvnw.cmd spring-boot:run`     |
