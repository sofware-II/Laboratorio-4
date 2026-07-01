# Guía de Comandos y Ejecución del Proyecto

Este documento reúne los comandos esenciales para trabajar con el módulo backend del proyecto (`backend-springboot`), incluyendo el control de versiones en Git, la compilación de Maven, la ejecución de pruebas automatizadas y el arranque del servidor de desarrollo para acceder a la documentación de Swagger UI.

---

## 1. Control de Versiones (Git)

Para trabajar de manera aislada en la documentación y organización de evidencias del **Punto 3**, se debe crear o cambiar a la rama de trabajo asignada:

```bash
git checkout -b docs/testing
```
*(Si la rama ya fue creada previamente, ejecute simplemente `git checkout docs/testing`).*

---

## 2. Ubicación en el Módulo Backend

El proyecto es un monorepo que contiene múltiples módulos (backend, frontend y pruebas funcionales). Todos los comandos Maven del backend deben ejecutarse ubicándose previamente en la carpeta del módulo Spring Boot:

```bash
cd backend-springboot
```

> **¡Importante para usuarios de Windows o sistemas sin Maven en PATH!**  
> Si su terminal no reconoce el comando `mvn`, utilice el **Maven Wrapper (`mvnw.cmd`)** que se encuentra en la raíz del directorio `backend-springboot/`.  
> En los ejemplos siguientes, reemplace `mvn` por `.\mvnw.cmd` (ej. `.\mvnw.cmd clean install`). En Linux/macOS reemplace por `./mvnw`.

---

## 3. Compilación y Validación Completa

Para limpiar archivos temporales previos, compilar el código fuente de Java 21, generar los adaptadores de MapStruct/Lombok, ejecutar toda la suite de pruebas unitarias e empaquetar el ejecutable `.jar`, ejecute:

```bash
mvn clean install
```

> **Comando alternativo en Windows con Maven Wrapper:**
> ```powershell
> .\mvnw.cmd clean install
> ```

---

## 4. Ejecución de Pruebas Unitarias

Si desea correr únicamente la batería de pruebas automatizadas con JUnit 5 y generar el informe de cobertura de código (JaCoCo) sin reconstruir el paquete final:

```bash
mvn test
```

> **Comando alternativo en Windows con Maven Wrapper:**
> ```powershell
> .\mvnw.cmd test
> ```

---

## 5. Levantar el Backend (Servidor de Desarrollo)

Para iniciar el servidor local de Spring Boot en modo desarrollo, cargando la configuración de la base de datos PostgreSQL/H2 y las migraciones de Flyway:

```bash
mvn spring-boot:run
```

> **Comando alternativo en Windows con Maven Wrapper:**
> ```powershell
> .\mvnw.cmd spring-boot:run
> ```
> *(El servidor web Tomcat embebido iniciará de forma predeterminada en el puerto `8080`).*

---

## 6. Acceder a Swagger UI (Documentación Interactiva)

Una vez que el backend se encuentre en ejecución mediante el comando anterior, abra un navegador web y acceda a la interfaz gráfica de Swagger UI para probar los endpoints interactivamente en:

**[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Para consultar o descargar la especificación cruda de la API en formato OpenAPI 3 (JSON), acceda a:
- [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Resumen de Comandos Rápidos

| Acción | Comando Estándar | Comando en Windows (Wrapper) |
| :--- | :--- | :--- |
| **Cambiar de rama Git** | `git checkout -b docs/testing` | `git checkout -b docs/testing` |
| **Entrar al módulo backend** | `cd backend-springboot` | `cd backend-springboot` |
| **Compilar e instalar** | `mvn clean install` | `.\mvnw.cmd clean install` |
| **Correr pruebas unitarias** | `mvn test` | `.\mvnw.cmd test` |
| **Levantar servidor local** | `mvn spring-boot:run` | `.\mvnw.cmd spring-boot:run` |
| **Abrir Swagger UI** | *Navegador:* `http://localhost:8080/swagger-ui/index.html` | *Navegador:* `http://localhost:8080/swagger-ui/index.html` |
