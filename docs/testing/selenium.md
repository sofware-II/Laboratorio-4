# 4. Pruebas Funcionales con Selenium

## Introducción

Las pruebas funcionales fueron implementadas utilizando **Selenium WebDriver con JUnit 5**, con el objetivo de validar el funcionamiento completo del sistema Task Manager a nivel de interfaz de usuario.

Estas pruebas simulan el comportamiento de un usuario real interactuando con la aplicación web, verificando la integración entre frontend (React), backend (Spring Boot) y base de datos (PostgreSQL).

---

## Tecnologías utilizadas

- Selenium WebDriver 4
- JUnit 5
- ChromeDriver
- Maven
- React (Frontend)
- Spring Boot (Backend)
- PostgreSQL

---

## Casos de prueba ejecutados

Se validaron los siguientes escenarios funcionales:

- Carga de la página principal (Projects)
- Creación de proyectos desde la interfaz web
- Listado de proyectos
- Actualización de proyectos
- Eliminación de proyectos
- Navegación entre vistas

---

## Ejecución de pruebas

Las pruebas se ejecutaron desde el módulo `functional-tests` utilizando Maven:

```bash
mvn clean test