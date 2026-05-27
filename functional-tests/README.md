# Lab 05 — Selenium WebDriver + JUnit

Configuración base de pruebas funcionales con **Selenium 4.26** y **JUnit 5** para Task Manager.

Esta rama contiene solo la **infraestructura Selenium** y pruebas de navegación inicial.

## Contenido

```
functional-tests/
├── pom.xml
├── .gitignore
└── src/test/java/dev/selenium/
    ├── support/WebDriverFactory.java
    └── getting_started/UsingSeleniumXUnitTest.java
```

## Prerrequisitos

- Java 17+
- Maven 3.9+
- Google Chrome
- Backend en `http://localhost:8080`
- Frontend en `http://localhost:3000`

## Ejecución

```bash
export JAVA_HOME="/opt/homebrew/Cellar/openjdk/25.0.2/libexec/openjdk.jdk/Contents/Home"
cd functional-tests
mvn test
```

Con navegador visible:

```bash
mvn test -Dselenium.headless=false
```

## Próximo paso

Tras mergear esta base en `desarrollo`, agregar en otra rama las pruebas funcionales del módulo **consulta y mantenimiento de proyectos** (Integrante 5).
