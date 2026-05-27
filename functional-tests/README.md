# Lab 05 — Pruebas funcionales de proyectos

Pruebas funcionales con **Selenium 4.26** y **JUnit 5** para el módulo **consulta y mantenimiento de proyectos** (Integrante 5).

Incluye la infraestructura Selenium base más los casos del módulo.

## Contenido

```
functional-tests/
├── pom.xml
├── .gitignore
├── docs/casos-de-prueba-proyectos.md
└── src/test/java/dev/selenium/
    ├── support/WebDriverFactory.java
    ├── getting_started/UsingSeleniumXUnitTest.java
    └── projects/ProjectMaintenanceFunctionalTest.java
```

## Prerrequisitos

- Java 17+
- Maven 3.9+
- Google Chrome
- Backend en `http://localhost:8080`
- Frontend en `http://localhost:3000`

## Ejecución

Solo pruebas del módulo proyectos:

```bash
export JAVA_HOME="/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home"
cd functional-tests
mvn test -Dtest=ProjectMaintenanceFunctionalTest
```

Todas las pruebas (navegación + proyectos):

```bash
mvn test
```

Con navegador visible:

```bash
mvn test -Dselenium.headless=false
```

## Casos cubiertos

| Tipo | Tests |
|------|-------|
| Consulta | listado, navegación a tareas del proyecto |
| Mantenimiento | editar, actualizar, eliminar, cancelar eliminación |

Detalle en [docs/casos-de-prueba-proyectos.md](docs/casos-de-prueba-proyectos.md).

## Dependencia

Esta rama parte de la infraestructura Selenium en `feature/lab-05-selenium-setup` (PR a `desarrollo`).
