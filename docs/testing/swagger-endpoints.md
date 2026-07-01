# Documentación de Endpoints y Swagger UI

La API REST del backend (`taskManager`) está completamente documentada bajo el estándar **OpenAPI 3.0** mediante la integración de la librería `springdoc-openapi-starter-webmvc-ui` (versión `2.8.9`). Esto permite a los desarrolladores y equipos de calidad visualizar, explorar y interactuar de forma interactiva con los endpoints del sistema sin necesidad de herramientas externas como Postman.

---

## Acceso a Swagger UI y OpenAPI
Con el servidor backend en ejecución, se puede acceder localmente a la documentación interactiva a través de las siguientes rutas web:
- **Interfaz Gráfica (Swagger UI):**  
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Especificación OpenAPI (JSON Raw):**  
  [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> **Nota de Configuración:** La habilitación de Swagger y OpenAPI está explícitamente declarada en el archivo `application.properties` mediante las directivas `springdoc.api-docs.enabled=true` y `springdoc.swagger-ui.enabled=true`.

---

## Catálogo de Endpoints por Controlador

A continuación se detallan los endpoints REST disponibles, agrupados según los controladores de la aplicación: `ProjectController` y `TaskController`. Todas las rutas base provienen de las definiciones centralizadas en la clase `ApiPaths`.

### 1. Controlador de Proyectos (`ProjectController`)
Ruta Base: `/projects`

#### 1.1 Crear Proyecto
- **Método HTTP:** `POST`
- **Ruta:** `/projects/add`
- **Descripción Breve:** Registra un nuevo proyecto en la base de datos del sistema.
- **Parámetros (Request Body JSON):**
  ```json
  {
    "name": "Nombre del Proyecto (Requerido, Texto)",
    "description": "Descripción detallada del proyecto (Opcional, Texto)"
  }
  ```
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna el `ProjectDto` creado incluyendo el `id` asignado automáticamente por el servidor.
  - **`400 Bad Request`**: Si el cuerpo de la petición incumple las validaciones de `@Valid` (por ejemplo, nombre nulo o vacío).

#### 1.2 Listar Todos los Proyectos
- **Método HTTP:** `GET`
- **Ruta:** `/projects/all`
- **Descripción Breve:** Obtiene el listado completo de los proyectos existentes en el sistema.
- **Parámetros:** Ninguno.
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna un arreglo JSON de objetos `ProjectDto`. Devuelve un arreglo vacío `[]` si no existen registros.

#### 1.3 Obtener Proyecto por ID
- **Método HTTP:** `GET`
- **Ruta:** `/projects/{id}`
- **Descripción Breve:** Busca y devuelve los detalles de un proyecto específico identificándolo por su ID numérico.
- **Parámetros (Path Variable):**
  - `id` (Long): Identificador único del proyecto (ej. `/projects/1`).
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna el objeto `ProjectDto` correspondiente.
  - **`404 Not Found`**: Si no existe ningún proyecto asociado a ese `id` (lanza `ResourceNotFoundException`).

#### 1.4 Actualizar Proyecto
- **Método HTTP:** `PUT`
- **Ruta:** `/projects/update/{id}`
- **Descripción Breve:** Modifica el nombre o la descripción de un proyecto existente.
- **Parámetros:**
  - **Path Variable:** `id` (Long) - ID del proyecto a editar.
  - **Request Body JSON (`ProjectDto`):**
    ```json
    {
      "name": "Nombre actualizado",
      "description": "Descripción modificada"
    }
    ```
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna el objeto `ProjectDto` con los datos actualizados tras guardarse en la base de datos.
  - **`404 Not Found`**: Si el `id` no corresponde a un proyecto existente.
  - **`400 Bad Request`**: En caso de error en la validación de los datos enviados.

#### 1.5 Eliminar Proyecto
- **Método HTTP:** `DELETE`
- **Ruta:** `/projects/delete/{id}`
- **Descripción Breve:** Elimina de forma definitiva el registro de un proyecto en la base de datos por su ID.
- **Parámetros (Path Variable):**
  - `id` (Long): Identificador único del proyecto a suprimir.
- **Respuesta Esperada:**
  - **`204 No Content`**: Eliminación exitosa, no se retorna cuerpo en la respuesta.
  - **`404 Not Found`**: Si el proyecto especificado no existe.

---

### 2. Controlador de Tareas (`TaskController`)
Ruta Base: `/tasks`

#### 2.1 Crear Tarea
- **Método HTTP:** `POST`
- **Ruta:** `/tasks/add`
- **Descripción Breve:** Crea una nueva tarea y la vincula obligatoriamente a un proyecto existente en el backend.
- **Parámetros (Request Body JSON):**
  ```json
  {
    "title": "Título de la tarea (Requerido)",
    "description": "Descripción de las actividades a realizar (Opcional)",
    "status": "Estado inicial (Valores: TODO, IN_PROGRESS, DONE)",
    "priority": "Prioridad asignada (Valores: LOW, MEDIUM, HIGH)",
    "projectId": 10
  }
  ```
  *(Nota: Los campos `status` y `priority` son validados canónicamente mediante la clase `TaskValidator`).*
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna el `TaskDto` recién persistido con su `id` generado.
  - **`404 Not Found`**: Si el `projectId` enviado en el JSON no existe en la tabla de proyectos.
  - **`400 Bad Request`**: Si los datos no cumplen con las validaciones declarativas.

#### 2.2 Obtener Tareas por Proyecto
- **Método HTTP:** `GET`
- **Ruta:** `/tasks/project/{projectId}`
- **Descripción Breve:** Recupera la lista de todas las tareas que pertenecen a un ID de proyecto específico.
- **Parámetros (Path Variable):**
  - `projectId` (Long): ID del proyecto contenedor.
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna un arreglo JSON de objetos `TaskDto`. Retorna una lista vacía `[]` si el proyecto no tiene tareas asociadas.

#### 2.3 Obtener Tarea por ID
- **Método HTTP:** `GET`
- **Ruta:** `/tasks/{id}`
- **Descripción Breve:** Consulta los detalles individuales de una tarea mediante su llave primaria.
- **Parámetros (Path Variable):**
  - `id` (Long): ID unívoco de la tarea.
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna el objeto `TaskDto`.
  - **`404 Not Found`**: Lanza excepción si el ID de la tarea no es localizado en la base de datos.

#### 2.4 Actualizar Tarea
- **Método HTTP:** `PUT`
- **Ruta:** `/tasks/update/{id}`
- **Descripción Breve:** Actualiza el título, descripción, estado o prioridad de una tarea.
- **Parámetros:**
  - **Path Variable:** `id` (Long) - ID de la tarea a actualizar.
  - **Request Body JSON (`TaskDto`):**
    ```json
    {
      "title": "Título Modificado",
      "description": "Nueva descripción",
      "status": "DONE",
      "priority": "HIGH",
      "projectId": 10
    }
    ```
- **Respuesta Esperada:**
  - **`200 OK`**: Retorna la tarea con la información actualizada.
  - **`400 Bad Request` / `IllegalArgumentException`**: Si se envía un valor de `status` o `priority` inválido que no coincida con las enumeraciones admitidas.
  - **`404 Not Found`**: Si el identificador numérico de la tarea no está presente en el sistema.

#### 2.5 Eliminar Tarea
- **Método HTTP:** `DELETE`
- **Ruta:** `/tasks/delete/{id}`
- **Descripción Breve:** Elimina permanentemente una tarea específica de la base de datos.
- **Parámetros (Path Variable):**
  - `id` (Long): Identificador numérico de la tarea.
- **Respuesta Esperada:**
  - **`204 No Content`**: Operación exitosa sin cuerpo en la respuesta.
  - **`404 Not Found`**: Si la tarea no existe al momento de solicitar su borrado.

---

## Evidencias
Para constatar el funcionamiento correcto y la visualización de todos los endpoints en el navegador mediante Swagger UI, consulte la imagen de evidencia almacenada en la ruta correspondiente:

![Evidencia Swagger UI](docs/testing/evidencias/swagger-ui.png)

*Referencia del archivo de imagen:* `docs/testing/evidencias/swagger-ui.png`
