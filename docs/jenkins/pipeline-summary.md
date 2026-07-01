# Pipeline CI/CD con Jenkins

## 1. Objetivo

El objetivo de esta sección es documentar la implementación del pipeline de integración y despliegue continuo del proyecto Task Manager.

El pipeline permite automatizar la construcción del backend, la compilación del frontend, la ejecución de pruebas, el análisis estático de código, la validación funcional, las pruebas no funcionales y la preparación del despliegue mediante Docker.

## 2. Archivo principal del pipeline

El pipeline fue definido mediante el archivo:

```text
Jenkinsfile