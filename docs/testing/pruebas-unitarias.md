# Documentación de Pruebas Unitarias e Integración

El aseguramiento de calidad del backend de `taskManager` se fundamenta en una batería completa de pruebas automatizadas creadas sobre el estándar **JUnit 5 (Jupiter)**, en combinación con el marco de simulación **Mockito** y las utilidades de prueba de **Spring Boot Test (`@WebMvcTest`, `@DataJpaTest` / `@SpringBootTest`, `MockMvc`)**.

---

## Clases y Componentes Evaluados
La suite de pruebas automatizadas verifica la integridad de los dos módulos clave del sistema (`task` y `project`), cubriendo las capas de modelo de dominio, acceso a datos (JPA), servicios de negocio y controladores REST. En total, se ejecutan **42 pruebas unitarias e integración** distribuidas en las siguientes clases:

| Clase de Prueba | Capa Evaluada | Descripción Breve |
| :--- | :--- | :--- |
| `TaskServiceTest` | Servicio / Negocio | Pruebas unitarias aisladas de la lógica en `TaskServiceImpl`, verificando CRUD, mapeo DTO y reglas de negocio. |
| `TaskControllerTest` | Presentación / REST | Pruebas de capa web (`@WebMvcTest`) de `TaskController` usando `MockMvc` para simular peticiones HTTP y serialización JSON. |
| `TaskRepositoryTest` | Repositorio / JPA | Pruebas de integración sobre consultas personalizadas (`findByProjectId`) y persistencia en base de datos H2 en memoria. |
| `ProjectServiceTest` | Servicio / Negocio | Pruebas unitarias de la lógica de aplicación en `ProjectServiceImpl` con dependencias simuladas mediante Mockito. |
| `ProjectControllerTest` | Presentación / REST | Pruebas de endpoints REST del controlador `ProjectController`, verificando estados HTTP y estructura de respuestas JSON. |
| `ProjectRepositoryTest` | Repositorio / JPA | Verificación de persistencia, consultas e integridad referencial sobre entidades de dominio `Project`. |
| `ProjectTest` | Modelo de Dominio | Validación unitaria de getters, setters, constructores y estado de la entidad de modelo `Project`. |
| `TaskManagerApplicationTests` | Configuración | Prueba de humo (Smoke Test) que valida la carga correcta del contexto de Spring Boot en el contenedor de inversión de control. |

---

## Qué Valida Cada Grupo de Pruebas

### 1. Pruebas de la Capa de Servicios (`TaskServiceTest`, `ProjectServiceTest`)
- **Éxito en operaciones CRUD:** Valida la correcta interconexión entre la recepción de un DTO, el mapeo hacia la entidad JPA por medio de MapStruct, el guardado en el repositorio y la devolución del DTO resultante.
- **Manejo de excepciones de negocio:** Asegura que al intentar consultar, modificar o eliminar un recurso inexistente, el servicio lance de inmediato una excepción `ResourceNotFoundException` con el mensaje adecuado (ej. *"Project not found with ID: 10"*).
- **Validaciones de enumeraciones:** En `TaskServiceTest`, se constata que al actualizar una tarea con estados (`status`) o prioridades (`priority`) inválidos, el validador `TaskValidator` intercepte la solicitud y lance un `IllegalArgumentException`, previniendo datos corruptos en la base de datos.
- **Consultas con resultados vacíos:** Confirma que al solicitar las tareas de un proyecto sin elementos asociados, el método retorne una lista inmutable vacía sin generar excepciones de puntero nulo (`NullPointerException`).

### 2. Pruebas de la Capa de Controladores REST (`TaskControllerTest`, `ProjectControllerTest`)
- **Contratos REST y Códigos de Estado:** A través de `MockMvc`, se envían peticiones HTTP simuladas (`GET`, `POST`, `PUT`, `DELETE`) hacia las rutas configuradas en `ApiPaths`.
- **Serialización y Deserialización JSON:** Se verifica utilizando `jsonPath` que las respuestas del servidor contengan exactamente los nombres de los atributos y valores esperados en los cuerpos JSON, corroborando también los encabezados de tipo de contenido (`application/json`).
- **Aislamiento de la Web:** Al utilizar `@WebMvcTest` combinada con `@MockitoBean`, se aíslan los controladores del acceso real a base de datos, simulando las respuestas de los servicios mediante `when(...).thenReturn(...)` para medir un rendimiento ágil y rápido.

### 3. Pruebas de la Capa de Repositorios y Dominio (`TaskRepositoryTest`, `ProjectRepositoryTest`, `ProjectTest`)
- **Persistencia H2 en Memoria:** Se ejecutan inserciones y consultas reales sobre un motor de base de datos relacional ligero en memoria (H2), verificando que las llaves primarias autogeneradas e integridad referencial (`@ManyToOne` hacia proyecto) operen sin fallos.
- **Métodos Personalizados JPA:** Evalúa que el método `findByProjectId(Long id)` de `TaskRepository` recupere exclusivamente las tareas vinculadas al proyecto indicado en la consulta SQL subyacente.

---

## Ejecución de las Pruebas Unitarias

Para ejecutar el conjunto completo de pruebas unitarias y generar los informes de cobertura sin empaquetar el artefacto final, se debe utilizar la terminal dentro del directorio del módulo backend (`backend-springboot/`) ejecutando el siguiente comando Maven:

```bash
mvn test
```

> **Nota para entornos Windows o sin Maven global en PATH:** Si el sistema operativo no tiene la variable de entorno `mvn` configurada globalmente, se debe ejecutar el wrapper nativo de Maven incluido en la raíz del proyecto:
> ```powershell
> .\mvnw.cmd test
> ```

### Resultado Esperado
Al lanzar la ejecución del comando de pruebas, la consola mostrará el inicio del ciclo de vida de Maven, levantando el contexto ligero de Spring Boot para cada clase de prueba. El resultado final exitoso debe presentar un resumen indicando **`BUILD SUCCESS`** sin fallos ni errores:

```text
[INFO] Results:
[INFO] 
[WARNING] Tests run: 42, Failures: 0, Errors: 0, Skipped: 1
[INFO] 
[INFO] --- jacoco:0.8.12:report (report) @ taskManager ---
[INFO] Loading execution data file ...\backend-springboot\target\jacoco.exec
[INFO] Analyzed bundle 'taskManager' with 17 classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
*(Nota: La única prueba que aparece como `Skipped` corresponde a la prueba de contexto base `TaskManagerApplicationTests`, habiéndose ejecutado 41 pruebas unitarias e integración de lógica e interfaz de forma satisfactoria).*

### Evidencia de Ejecución (`mvn test`)
La evidencia visual confirmando el éxito de la ejecución del comando `mvn test` en terminal se encuentra referenciada a continuación:

![Evidencia de ejecución mvn test](docs/testing/evidencias/mvn-test.png)

*Referencia del archivo de imagen:* `docs/testing/evidencias/mvn-test.png`

---

## Validación Completa del Proyecto (`mvn clean install`)

Como parte del ciclo de aseguramiento continuo, es obligatorio validar que todo el proyecto compile desde cero, ejecute la totalidad de sus pruebas automatizadas y empaque el artefacto ejecutable (`.jar`) sin advertencias ni bloqueos. Para este propósito se utiliza el comando:

```bash
mvn clean install
```

> **En Windows mediante Maven Wrapper:**
> ```powershell
> .\mvnw.cmd clean install
> ```

### Explicación del Proceso y Resultado
Este comando ejecuta el siguiente ciclo completo:
1. **`clean`:** Elimina el directorio `target/` previo, garantizando una compilación limpia sin artefactos en caché.
2. **Compilación y Mapeo:** Compila el código fuente en Java 21, disparando los procesadores de anotaciones de **Lombok** y **MapStruct** para autogenerar las clases de implementación (como `TaskMapperImpl` y `ProjectMapperImpl`).
3. **`test`:** Ejecuta la batería de las 42 pruebas descritas anteriormente y genera el reporte de cobertura en `target/site/jacoco/index.html`.
4. **`package` / `install`:** Genera el archivo ejecutable `taskManager-0.0.1-SNAPSHOT.jar` empaquetado con todas las dependencias embebidas y lo instala en el repositorio local de Maven (`~/.m2/repository`).

El resultado esperado en terminal concluye con el mensaje final de confirmación:

```text
[INFO] --- install:3.1.4:install (default-install) @ taskManager ---
[INFO] Installing ...\backend-springboot\pom.xml to ...\.m2\repository\...
[INFO] Installing ...\backend-springboot\target\taskManager-0.0.1-SNAPSHOT.jar to ...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

### Evidencia de Validación (`mvn clean install`)
La captura de pantalla constatando el éxito de la compilación e instalación limpia del proyecto en el entorno local se adjunta a continuación:

![Evidencia de ejecución mvn clean install](docs/testing/evidencias/mvn-clean-install.png)

*Referencia del archivo de imagen:* `docs/testing/evidencias/mvn-clean-install.png`
