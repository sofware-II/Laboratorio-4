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