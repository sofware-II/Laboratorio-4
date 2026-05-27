# Casos de Prueba - Actualización de Tareas

## Resumen
Documento tabular de casos de prueba para la funcionalidad de actualización de tareas en el sistema Task Manager.

---

## Tabla de Casos de Prueba

| # | Objeto de Prueba | Funcionalidad | Escenario/Acciones | Valores de Prueba | Resultado Esperado |
|---|---|---|---|---|---|
| 1 | TaskService | Actualizar Título | Usuario actualiza el título de una tarea existente | `TaskId: 1, NuevoTítulo: "Nuevo Título"` | El título se actualiza correctamente, otros campos permanecen sin cambios |
| 2 | TaskService | Actualizar Descripción | Usuario actualiza la descripción de una tarea existente | `TaskId: 1, NuevaDesc: "Nueva descripción"` | La descripción se actualiza correctamente, otros campos permanecen sin cambios |
| 3 | TaskService | Actualizar Estado | Usuario cambia el estado de una tarea (TODO → IN_PROGRESS) | `TaskId: 1, NuevoEstado: "IN_PROGRESS"` | El estado se actualiza correctamente a IN_PROGRESS |
| 4 | TaskService | Actualizar Prioridad | Usuario cambia la prioridad de una tarea (MEDIUM → HIGH) | `TaskId: 1, NuevaPrioridad: "HIGH"` | La prioridad se actualiza correctamente a HIGH |
| 5 | TaskService | Actualizar Múltiples Campos | Usuario actualiza título, descripción, estado y prioridad simultáneamente | `TaskId: 1, Título: "Updated", Desc: "New Desc", Status: "DONE", Priority: "LOW"` | Todos los campos se actualizan correctamente |
| 6 | TaskService | Tarea No Encontrada | Usuario intenta actualizar una tarea que no existe | `TaskId: 999` | Se lanza excepción ResourceNotFoundException |
| 7 | TaskService | Estado Inválido | Usuario intenta establecer un estado inválido | `TaskId: 1, Estado: "INVALID_STATUS"` | Se lanza excepción IllegalArgumentException |
| 8 | TaskService | Prioridad Inválida | Usuario intenta establecer una prioridad inválida | `TaskId: 1, Prioridad: "INVALID_PRIORITY"` | Se lanza excepción IllegalArgumentException |
| 9 | TaskService | Actualizar Campos Nulos | Usuario envía campos nulos en la actualización | `TaskId: 1, Campos: null` | Los campos nulos no se actualizan, la tarea mantiene sus valores originales |
| 10 | TaskController | PUT /tasks/update/{id} | Actualización vía REST API | `PUT /tasks/update/1 con TaskDto actualizado` | API retorna HTTP 200 con TaskDto actualizado en JSON |
| 11 | TaskRepository | Persistencia en BD | Confirmar que cambios se guardan en base de datos | `TaskId: 1 actualizado en BD` | Los datos se persisten correctamente en PostgreSQL |
| 12 | TaskMapper | Mapeo DTO a Entity | Validar que el mapeo de TaskDto a Task se realiza correctamente | `TaskDto → Task Entity` | El mapeo preserva todos los campos correctamente |

---

## Enumeraciones y Valores Válidos

### TaskStatus
- `TODO`
- `IN_PROGRESS`
- `DONE`

### TaskPriority
- `LOW`
- `MEDIUM`
- `HIGH`

---

## Ejecución de Pruebas

### Ejecutar todos los tests unitarios
```bash
cd backend-springboot
mvn test
```

### Ejecutar solo tests de actualización
```bash
cd backend-springboot
mvn test -Dtest=TaskUpdateServiceTest
```

### Ejecutar con cobertura de código
```bash
cd backend-springboot
mvn clean test jacoco:report
```

### Reporte de JaCoCo
Ubicación: `target/site/jacoco/index.html`

### Ejecutar tests de integración
```bash
cd backend-springboot
mvn test -Dtest=TaskUpdateControllerIntegrationTest
```

### Ejecutar tests funcionales (Selenium)
```bash
cd functional-tests
mvn test -Dtest=TaskUpdateSeleniumTest
```

---

## Marcos xUnit Utilizados

- **JUnit 5 (Jupiter)**: Anotaciones @Test, @BeforeEach, @AfterEach
- **Mockito**: Mocking y verificación de interacciones
- **AssertJ**: Assertions fluidas y legibles
- **Spring Boot Test**: Testing del contexto de la aplicación
- **MockMvc**: Testing de endpoints REST
- **Selenium WebDriver**: Automatización de pruebas funcionales
