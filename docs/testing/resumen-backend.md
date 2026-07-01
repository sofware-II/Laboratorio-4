# Resumen y Arquitectura del Backend

El backend del sistema de gestión de tareas (`taskManager`) actúa como el núcleo central de procesamiento de datos y reglas de negocio del proyecto. Expone una API RESTful construida con Spring Boot que permite a los clientes (como la interfaz de usuario en ReactJS) interactuar de forma segura, estructurada y eficiente con los recursos del sistema: **Proyectos** y **Tareas**.

---

## Rol del Backend en el Proyecto
1. **Gestión Centralizada de Negocio:** Encapsula la lógica para la creación, validación, modificación y eliminación de proyectos y tareas asociadas.
2. **Abstracción de Datos:** Separa la estructura interna de la base de datos relacional de los datos presentados al cliente mediante Objetos de Transferencia de Datos (DTOs).
3. **Integridad y Seguridad de Datos:** Aplica validaciones estrictas sobre los datos de entrada (por ejemplo, estados y prioridades permitidas para las tareas) antes de persistirlos en la base de datos.
4. **Interoperabilidad:** Expone contratos HTTP claros, predecibles y documentados mediante el estándar OpenAPI/Swagger, facilitando la integración con el frontend.

---

## Arquitectura General del Backend
El proyecto implementa una **Arquitectura en Capas (Layered Architecture)** modular que incorpora principios de diseño limpio y separación de responsabilidades (Separation of Concerns). Se observan dos estilos arquitectónicos complementarios dentro del código:
- **Arquitectura en Capas Tradicional (Módulo de Tareas):** Estructurada directamente en paquetes funcionales (`controller`, `service`, `repository`, `entity`, `dto`, `mapper`).
- **Arquitectura Limpia / Orientada al Dominio (Módulo de Proyectos):** Estructurada en subpaquetes que delimitan las responsabilidades de dominio y presentación (`project.presentation`, `project.application`, `project.domain`, `project.infrastructure`).

### Capas Principales
1. **Capa de Presentación / Controladores (`controller` / `presentation`):**
   - **Clases:** `TaskController`, `ProjectController`, `ControllerResponseBuilder`, `ApiPaths`.
   - **Responsabilidad:** Intercepta las peticiones HTTP entrantes, valida los cuerpos de las solicitudes mediante anotaciones de Jakarta Validation (`@Valid`, `@RequestBody`), invoca los servicios correspondientes y estandariza las respuestas HTTP (códigos de estado 200 OK, 204 No Content, etc.).
2. **Capa de Aplicación / Servicios (`service` / `application.service`):**
   - **Clases e Interfaces:** `TaskService` (`TaskServiceImpl`), `ProjectService` (`ProjectServiceImpl`).
   - **Responsabilidad:** Contiene las reglas de negocio de la aplicación. Coordinan la recuperación de entidades, ejecutan validaciones de negocio (utilizando componentes como `TaskValidator`) y orquestan la conversión entre Entidades JPA y DTOs.
3. **Capa de Acceso a Datos / Repositorios (`repository` / `domain.repository` / `infrastructure.persistence`):**
   - **Clases e Interfaces:** `TaskRepository`, `ProjectRepository`, `SpringDataProjectRepository`.
   - **Responsabilidad:** Extiende las interfaces de Spring Data JPA (`JpaRepository`) para gestionar la persistencia y recuperación de datos relacionales sin necesidad de escribir SQL manual para operaciones estándar, incorporando consultas personalizadas (por ejemplo, `findByProjectId`).
4. **Capa de Modelo / Entidades y DTOs (`entity` / `domain.model` / `dto` / `application.dto`):**
   - **Entidades JPA (`Task`, `Project`):** Representan la estructura física de las tablas en la base de datos relacional.
   - **DTOs (`TaskDto`, `ProjectDto`):** Definen la estructura de los payloads JSON intercambiados en la API REST, evitando la exposición no deseada de metadatos o relaciones bidireccionales complejas.
   - **Mappeadores (`TaskMapper`, `ProjectMapper`):** Componentes generados en tiempo de compilación por **MapStruct** que garantizan transformaciones eficientes y libres de errores entre entidades y DTOs.

---

## Flujo General de una Petición HTTP
El recorrido típico de una solicitud entrante (por ejemplo, crear o consultar una tarea) sigue el siguiente flujo secuencial:

```
[Cliente / Frontend]
        │
        ▼ (Petición HTTP REST JSON)
[Controller (TaskController / ProjectController)]
        │ 1. Validación de payload (@Valid)
        │ 2. Llamada a método de interfaz de servicio
        ▼
[Service (TaskServiceImpl / ProjectServiceImpl)]
        │ 3. Validación de reglas de negocio (TaskValidator)
        │ 4. Conversión DTO ──► Entidad JPA (a través de MapStruct)
        │ 5. Llamada a métodos de repositorio
        ▼
[Repository (TaskRepository / ProjectRepository)]
        │ 6. Traducción ORM (Hibernate) a consultas SQL
        ▼
[Base de Datos PostgreSQL / H2]
        │ 7. Ejecución de consulta y retorno de conjunto de resultados
        ▼
[Repository] ──► [Service] (Conversión Entidad JPA ──► DTO) ──► [Controller]
                                                                      │
        ▲ (Respuesta HTTP REST con payload JSON y código de estado)    │
        └─────────────────────────────────────────────────────────────┘
```

---

## Relación del Backend con la Base de Datos
El sistema utiliza un enfoque híbrido de persistencia robusto:
- **Motor Principal:** PostgreSQL configurado en el entorno de ejecución (`application.properties`).
- **Motor en Memoria para Pruebas:** H2 Database configurado en el alcance de pruebas (`test`), lo que permite ejecutar test unitarios e de integración de forma rápida y aislada sin depender de un servidor externo.
- **ORM (Hibernate / Spring Data JPA):** Mapea automáticamente las clases Java a las tablas `project` y `task`, gestionando las llaves primarias autoincrementales (`@GeneratedValue`) y las relaciones de clave foránea (`@ManyToOne` entre tarea y proyecto).
- **Evolución y Control de Versiones del Esquema (Flyway):** En lugar de depender exclusivamente de la autogeneración de Hibernate en producción, el proyecto integra **Flyway** (`spring.flyway.enabled=true`). Las migraciones históricas se almacenan ordenadamente dentro del directorio `src/main/resources/db/migration/`, garantizando que todos los entornos (desarrollo, pruebas y producción) tengan exactamente la misma estructura de base de datos de manera replicable.

---

## Buenas Prácticas Aplicadas
1. **Inyección de Dependencias por Constructor:** Uso de la anotación `@RequiredArgsConstructor` de Lombok en servicios y controladores para inyectar dependencias finales (`final`), promoviendo la inmutabilidad y facilitando las pruebas unitarias con Mockito.
2. **Separación DTO / Entidad:** Aislamiento total entre el modelo de persistencia y el modelo de API REST, utilizando MapStruct en tiempo de compilación para evitar penalizaciones de rendimiento por reflexión.
3. **Validación Declarativa y Centralizada:** Uso de anotaciones estándar (`@Valid`, `@NotNull`, `@Size`, etc.) combinadas con validadores de dominio dedicados (`TaskValidator`) que verifican la coherencia de enums como `TaskStatus` y `TaskPriority`.
4. **Manejo Excepcional Global:** Uso de `@ControllerAdvice` (`GlobalExceptionHandler`) para interceptar excepciones (como `ResourceNotFoundException` o errores de validación) y transformarlas automáticamente en respuestas HTTP estructuradas con códigos de estado semánticos (`404 Not Found`, `400 Bad Request`, `500 Internal Server Error`).
5. **Configuración de CORS Centralizada:** Definición de una política CORS flexible y configurable mediante `WebConfig`, permitiendo el acceso seguro desde orígenes específicos del frontend (por defecto, `http://localhost:3000`).
6. **Estandarización de Rutas REST:** Centralización de las rutas URI en la clase constante inmutable `ApiPaths`, evitando cadenas mágicas en las anotaciones de los controladores y previniendo errores de tipeo.
