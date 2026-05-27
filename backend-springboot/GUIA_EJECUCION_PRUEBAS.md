# Guía de Ejecución de Pruebas - Actualización de Tareas

## Descripción General

Este proyecto incluye pruebas unitarias, de integración y funcionales para la funcionalidad de **actualización de tareas**.

### Estructura de Pruebas

```
backend-springboot/src/test/java/com/hasbi/taskManager/
├── service/
│   ├── TaskServiceTest.java                    
│   └── TaskUpdateServiceTest.java          ✅ (10 test cases unitarios)
├── controller/
│   └── TaskUpdateControllerIntegrationTest.java ✅ (10 test cases de integración)
└── selenium/
    └── TaskUpdateSeleniumTest.java          ✅ (8 test cases end-to-end)
```

---

## 1. Pruebas Unitarias (JUnit + Mockito)

### Ubicación
`backend-springboot/src/test/java/com/hasbi/taskManager/service/TaskUpdateServiceTest.java`

### Casos de Prueba
- **TC01**: Actualizar título
- **TC02**: Actualizar descripción
- **TC03**: Cambiar estado a IN_PROGRESS
- **TC04**: Cambiar prioridad a HIGH
- **TC05**: Actualizar múltiples campos
- **TC06**: Tarea no encontrada (excepción)
- **TC07**: Estado inválido (excepción)
- **TC08**: Prioridad inválida (excepción)
- **TC09**: Campos nulos preservan originales
- **TC10**: Actualización completa del objeto

### Ejecución

#### Ejecutar solo tests unitarios
```bash
cd backend-springboot
mvn test -Dtest=TaskUpdateServiceTest
```

#### Ejecutar con salida detallada
```bash
mvn test -Dtest=TaskUpdateServiceTest -X
```

#### Ejecutar con cobertura de código
```bash
mvn clean test -Dtest=TaskUpdateServiceTest jacoco:report
```

#### Ver reporte de cobertura
```bash
# Después de ejecutar con cobertura
target/site/jacoco/index.html
```

---

## 2. Pruebas de Integración (Spring Boot Test + MockMvc)

### Ubicación
`backend-springboot/src/test/java/com/hasbi/taskManager/controller/TaskUpdateControllerIntegrationTest.java`

### Casos de Prueba
- **TC-API-01**: PUT /tasks/update/{id} - Actualizar título (HTTP 200)
- **TC-API-02**: PUT /tasks/update/{id} - Actualizar descripción (HTTP 200)
- **TC-API-03**: PUT /tasks/update/{id} - Cambiar estado (HTTP 200)
- **TC-API-04**: PUT /tasks/update/{id} - Cambiar prioridad (HTTP 200)
- **TC-API-05**: PUT /tasks/update/{id} - Múltiples campos (HTTP 200)
- **TC-API-06**: Tarea no encontrada (HTTP 404)
- **TC-API-07**: Estado inválido (HTTP 400)
- **TC-API-08**: Prioridad inválida (HTTP 400)
- **TC-API-09**: Campos nulos preservan originales
- **TC-API-10**: Respuesta contiene el ID de la tarea

### Ejecución

#### Ejecutar solo tests de integración
```bash
cd backend-springboot
mvn test -Dtest=TaskUpdateControllerIntegrationTest
```

#### Ejecutar con salida detallada
```bash
mvn test -Dtest=TaskUpdateControllerIntegrationTest -X
```

---

## 3. Pruebas Funcionales (Selenium WebDriver)

### Ubicación
`backend-springboot/src/test/java/com/hasbi/taskManager/selenium/TaskUpdateSeleniumTest.java`

### Casos de Prueba
- **TC01-UI**: Actualizar título desde interfaz
- **TC02-UI**: Actualizar descripción desde UI
- **TC03-UI**: Cambiar estado desde UI
- **TC04-UI**: Cambiar prioridad desde UI
- **TC05-UI**: Validar campos requeridos
- **TC06-UI**: Cancelar cambios no guarda datos
- **TC07-UI**: Actualizar múltiples campos
- **TC08-UI**: Buscar y actualizar tarea

### Prerequisitos para Selenium

1. **Iniciar el backend (Spring Boot)**
```bash
cd backend-springboot
mvn spring-boot:run
# O
./mvnw spring-boot:run
```

2. **Iniciar el frontend (React)**
```bash
cd frontend-reactjs
npm start
# La aplicación abrirá en http://localhost:3000
```

3. **Chrome/Chromium instalado** (WebDriverManager lo descargará automáticamente)

### Ejecución

#### Ejecutar tests de Selenium
```bash
cd backend-springboot
mvn test -Dtest=TaskUpdateSeleniumTest
```

#### Ejecutar con log detallado
```bash
mvn test -Dtest=TaskUpdateSeleniumTest -X
```

#### Ejecutar solo un test específico
```bash
mvn test -Dtest=TaskUpdateSeleniumTest#testUpdateTaskTitle_FromUI
```

---

## 4. Ejecutar TODAS las Pruebas

### Ejecutar todo (unitarias + integración + funcionales)
```bash
cd backend-springboot
mvn clean test
```

### Ejecutar solo sin funcionales (Selenium)
```bash
cd backend-springboot
mvn clean test -Dtest=TaskUpdateServiceTest,TaskUpdateControllerIntegrationTest
```

### Ejecutar y generar reporte
```bash
cd backend-springboot
mvn clean test jacoco:report
# Ver reporte en: target/site/jacoco/index.html
```

---

## 5. Marcos xUnit Utilizados

### JUnit 5 (Jupiter)
- ✅ Anotaciones: `@Test`, `@BeforeEach`, `@AfterEach`, `@DisplayName`
- ✅ Assertions: AssertJ (`assertThat`, `assertEquals`, `assertTrue`, etc.)
- ✅ Excepciones: `assertThatThrownBy`

### Mockito
- ✅ Mocking: `@Mock`, `@InjectMocks`
- ✅ Verificación: `verify`, `never`, `times`
- ✅ Stubbing: `when().thenReturn()`

### Spring Boot Test
- ✅ Context: `@SpringBootTest`
- ✅ Web Testing: `@AutoConfigureMockMvc`, `MockMvc`
- ✅ Transacciones: `@Transactional`

### Selenium WebDriver
- ✅ Driver: ChromeDriver (gestión automática con WebDriverManager)
- ✅ Locators: By.id, By.css, By.xpath
- ✅ Waits: WebDriverWait, implicitlyWait
- ✅ Interact: click(), sendKeys(), select()

---

## 6. Configuración de Dependencias (pom.xml)

Las siguientes dependencias ya están incluidas:

```xml
<!-- JUnit 5 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Selenium WebDriver -->
<dependency>
    <groupId>org.seleniumhq.selenium</groupId>
    <artifactId>selenium-java</artifactId>
    <version>4.15.0</version>
    <scope>test</scope>
</dependency>

<!-- WebDriverManager (manejo automático de ChromeDriver) -->
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.6.3</version>
    <scope>test</scope>
</dependency>

<!-- JaCoCo (cobertura de código) -->
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
</plugin>
```

---

## 7. Troubleshooting

### Error: "Task not found with ID"
- Verificar que la tarea existe en base de datos
- Verificar que el ID en la URL es correcto

### Error: "Invalid task status"
- Valores válidos: `TODO`, `IN_PROGRESS`, `DONE`
- Verificar que no hay espacios extra

### Error: "Invalid task priority"
- Valores válidos: `LOW`, `MEDIUM`, `HIGH`
- Verificar que no hay espacios extra

### Selenium: "ChromeDriver not found"
- WebDriverManager debería descargarlo automáticamente
- Si falla, instalar Chrome/Chromium manualmente

### Selenium: "Element not found"
- Verificar que el frontend está ejecutándose en http://localhost:3000
- Verificar que los data-testid coinciden
- Aumentar el timeout en setUp()

---

## 8. Entregables

✅ **Casos de Prueba Identificados**
- Documento: `docs/CASOS_PRUEBA_ACTUALIZACION_TAREAS.md`

✅ **Código de Pruebas Unitarias (JUnit)**
- Clase: `TaskUpdateServiceTest.java` (10 test cases)
- Método de prueba por caso: Sí (1 método = 1 test case)

✅ **Código de Pruebas de Integración**
- Clase: `TaskUpdateControllerIntegrationTest.java` (10 test cases)
- Spring Boot Test Context + MockMvc

✅ **Código de Pruebas Funcionales (Selenium)**
- Clase: `TaskUpdateSeleniumTest.java` (8 test cases)
- Automatización de UI con WebDriver

✅ **Reporte de Ejecución**
- Ejecutar: `mvn clean test jacoco:report`
- Ubicación: `target/site/jacoco/index.html`

---

## 9. Comandos Rápidos

```bash
# Compilar proyecto
mvn clean compile

# Ejecutar todos los tests
mvn clean test

# Ejecutar solo unitarios
mvn test -Dtest=TaskUpdateServiceTest

# Ejecutar solo integración
mvn test -Dtest=TaskUpdateControllerIntegrationTest

# Ejecutar solo Selenium
mvn test -Dtest=TaskUpdateSeleniumTest

# Generar reporte de cobertura
mvn clean test jacoco:report

# Ejecutar con Maven Wrapper (Linux/Mac)
./mvnw clean test

# Ejecutar con Maven Wrapper (Windows)
mvnw.cmd clean test
```

---

## Notas Importantes

⚠️ **No modificar `pom.xml`** sin revisar con el equipo (contiene dependencias compartidas)

⚠️ **Selenium requiere ambas aplicaciones ejecutándose**:
- Backend: `mvn spring-boot:run` en puerto 8080
- Frontend: `npm start` en puerto 3000

⚠️ **Las pruebas son independientes** y pueden ejecutarse en cualquier orden

✅ **Recomendación**: Ejecutar `mvn clean test` antes de hacer commits

---

**Última actualización**: 2026-05-27
**Versión**: 1.0
**Autor**: Task Manager Test Suite
