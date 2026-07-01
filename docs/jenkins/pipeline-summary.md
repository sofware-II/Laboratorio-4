# Pipeline CI/CD con Jenkins

## 1. Objetivo

El objetivo de esta sección es documentar la implementación del pipeline de integración y despliegue continuo del proyecto Task Manager.

El pipeline permite automatizar la construcción del backend, la compilación del frontend, la ejecución de pruebas, el análisis estático de código, la validación funcional, las pruebas no funcionales y la preparación del despliegue mediante Docker.

## 2. Archivo principal del pipeline

El pipeline fue definido mediante el archivo:



## Resultado del análisis SonarQube con cobertura

Se ejecutó correctamente el análisis estático del proyecto mediante SonarScanner. Además, se integró la lectura del reporte de cobertura generado por JaCoCo.

Inicialmente, SonarQube reportaba una cobertura de `0.0%`. Luego de configurar la lectura del reporte JaCoCo, SonarQube reconoció una cobertura de `67.9%`.

El Quality Gate continúa en estado `Failed`, debido a que la política configurada exige una cobertura mínima de `80.0%` sobre el código nuevo. Este resultado no corresponde a un error de ejecución del scanner ni del pipeline, sino a una observación de calidad relacionada con la cobertura de pruebas.

| Métrica | Resultado |
|---|---|
| SonarScanner | Ejecutado correctamente |
| JaCoCo | Integrado |
| Coverage inicial | 0.0% |
| Coverage actual | 67.9% |
| Coverage requerida | 80.0% |
| Quality Gate | Failed |
| New Issues | 0 |
| Accepted Issues | 0 |
| Duplications | 0.0% |
| Security Hotspots | 0 |

## Evidencias SonarQube

Las evidencias del análisis se encuentran en la carpeta `docs/jenkins/`.

Archivos de evidencia:

- `sonarqube-dashboard.png`
- `sonarqube-quality-gate-67.png`
- `sonarqube-measures.png`
- `sonarqube-issues.png`
- `sonarqube-scanner-success.png`

## Interpretación del resultado

El análisis SonarQube fue ejecutado correctamente. La integración con JaCoCo permitió que la cobertura pase de `0.0%` a `67.9%`. Sin embargo, el Quality Gate permanece en estado `Failed` porque el umbral configurado en SonarQube exige una cobertura mínima de `80.0%`.

## Acción pendiente

Se registra como mejora pendiente incrementar la cobertura de pruebas unitarias para alcanzar o superar el umbral de `80.0%`.

```text
Jenkinsfile

# Pipeline CI/CD con Jenkins

## Objetivo

Automatizar la construcción, pruebas y análisis del proyecto Task Manager mediante Jenkins.

## Etapas del pipeline inicial

| Stage | Descripción |
|---|---|
| Checkout | Descarga el código desde GitHub |
| Verify Tools | Verifica Java, Maven, Node, npm, Git y SonarScanner |
| Backend - Unit Tests | Ejecuta pruebas unitarias del backend |
| Frontend - Build | Construye el frontend React |
| SonarQube Analysis | Ejecuta análisis estático con SonarQube |

## Etapas condicionales

| Stage | Estado inicial |
|---|---|
| Functional Tests - Selenium | Desactivado hasta integración |
| Performance Tests - JMeter | Desactivado hasta que exista el archivo .jmx |
| Docker Build | Desactivado hasta que exista docker-compose.yml |

## Observación

El pipeline inicial se ejecuta sobre la rama `feature/final-pipeline-project-management`.  
Cuando las demás ramas sean integradas en `desarrollo`, el pipeline se ejecutará nuevamente sobre la rama `desarrollo`.
## Resultado de ejecución inicial

El pipeline inicial fue ejecutado correctamente desde Jenkins sobre la rama `feature/final-pipeline-project-management`.

### Etapas ejecutadas

| Etapa | Resultado |
|---|---|
| Checkout | Correcto |
| Verify Tools | Correcto |
| Backend - Unit Tests | Correcto |
| Frontend - Build | Correcto |
| SonarQube Analysis | Correcto |
| Functional Tests - Selenium | No ejecutado en esta primera integración |
| Performance Tests - JMeter | No ejecutado en esta primera integración |
| Docker Build | No ejecutado en esta primera integración |

### Evidencia principal

- Backend: compilación y pruebas unitarias ejecutadas correctamente.
- Frontend: build generado correctamente.
- SonarQube: análisis estático ejecutado correctamente.
- Pipeline: finalizó con estado exitoso.

Las etapas de Selenium, JMeter y Docker quedaron parametrizadas para ejecutarse cuando las ramas correspondientes sean integradas a `desarrollo`.