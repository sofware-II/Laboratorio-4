# Docker y despliegue — Task Manager

Documentación del módulo de containerización y despliegue del proyecto final.

**Rama:** `feature/final-docker-deployment`  
**Responsable:** Marcelo Juan Surco Salas  
**Fecha:** Julio 2026

---

## Resumen para Google Docs

El equipo implementó el despliegue automático de la aplicación **Task Manager** mediante **Docker** y **Docker Compose**, cumpliendo el requisito de gestión de entrega del pipeline CI/CD del proyecto final.

La solución containeriza tres componentes:

1. **PostgreSQL 16** como base de datos persistente.
2. **Backend Spring Boot 3.5** (Java 21) con API REST y Swagger.
3. **Frontend React** servido por **Nginx** en producción.

Se utilizó el patrón **multi-stage build** en ambos Dockerfiles para reducir el tamaño de las imágenes finales: una etapa de compilación (Maven / Node) y otra de ejecución (JRE / Nginx).

El archivo `docker-compose.yml` orquesta los tres servicios, define variables de entorno para la conexión a la base de datos, configura healthchecks y establece el orden de arranque (`depends_on` con `service_healthy`).

**Comandos principales:**

```bash
docker compose up -d --build   # Construir y levantar
docker compose ps              # Ver estado
docker compose logs -f         # Ver logs
docker compose down            # Detener y limpiar
```

**Verificación realizada:**

- Frontend accesible en `http://localhost:3000`
- Swagger UI en `http://localhost:8080/swagger-ui/index.html`
- API REST respondiendo en `http://localhost:8080/projects/all`
- Creación de proyectos desde la API dentro del entorno Docker

---

## Arquitectura de contenedores

```text
┌─────────────────────────────────────────────────────────────┐
│                     docker-compose.yml                      │
├──────────────┬──────────────────────┬───────────────────────┤
│   db         │      backend         │       frontend        │
│ PostgreSQL   │   Spring Boot JAR    │   Nginx + React build │
│ :5432        │   :8080              │   :3000 → :80         │
└──────┬───────┴──────────┬───────────┴───────────────────────┘
       │                  │
       └──── jdbc ────────┘
```

| Servicio   | Imagen base              | Puerto host | Descripción                          |
|------------|--------------------------|-------------|--------------------------------------|
| `db`       | `postgres:16-alpine`     | 5432        | Base de datos `task_manager_db`      |
| `backend`  | `eclipse-temurin:21-jre` | 8080        | API REST + Flyway + Swagger          |
| `frontend` | `nginx:alpine`           | 3000        | SPA React compilada en producción    |

---

## Dockerfile del backend

**Ubicación:** `backend-springboot/Dockerfile`

```dockerfile
# Etapa 1: compilación con Maven
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src
RUN chmod +x mvnw && ./mvnw package -DskipTests -B

# Etapa 2: imagen de ejecución
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Decisiones técnicas:**

- **Multi-stage build:** la imagen final solo contiene el JRE y el JAR, sin Maven ni código fuente.
- **Maven Wrapper (`mvnw`):** garantiza la misma versión de Maven en cualquier entorno.
- **`-DskipTests`:** acelera el build de la imagen; las pruebas se ejecutan en el pipeline CI/CD.
- **Variables de entorno en runtime:** Spring Boot sobrescribe `application.properties` con `SPRING_DATASOURCE_URL`, etc.

---

## Dockerfile del frontend

**Ubicación:** `frontend-reactjs/Dockerfile`

```dockerfile
# Etapa 1: compilación de la aplicación React
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY . .
ARG REACT_APP_API_BASE_URL=http://localhost:8080
ENV REACT_APP_API_BASE_URL=$REACT_APP_API_BASE_URL
RUN npm run build

# Etapa 2: servidor Nginx para archivos estáticos
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

**Decisiones técnicas:**

- **`npm ci`:** instalación determinista según `package-lock.json`.
- **`REACT_APP_API_BASE_URL`:** se inyecta en tiempo de build porque Create React App embebe las variables `REACT_APP_*` en el bundle.
- **Nginx:** sirve los archivos estáticos del build de producción y redirige rutas SPA a `index.html` (React Router).
- **Puerto interno 80** mapeado al **3000** del host para mantener consistencia con el desarrollo local.

---

## Explicación de `docker-compose.yml`

**Ubicación:** `docker-compose.yml` (raíz del repositorio)

### Servicio `db`

- Imagen oficial `postgres:16-alpine`.
- Crea la base `task_manager_db` con usuario `postgres`.
- Volumen `pgdata` para persistir datos entre reinicios.
- **Healthcheck** con `pg_isready` para confirmar que PostgreSQL acepta conexiones.

### Servicio `backend`

- Se construye desde `./backend-springboot`.
- Variables de entorno sobrescriben la configuración local:
  - `SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/task_manager_db` (hostname `db` = red interna Docker).
  - `APP_CORS_ALLOWED_ORIGINS=http://localhost:3000` (permite peticiones del navegador).
- **`depends_on` con `condition: service_healthy`:** el backend espera a que PostgreSQL esté listo antes de arrancar (evita errores de Flyway).
- **Healthcheck** verifica que Swagger UI responda en el puerto 8080.

### Servicio `frontend`

- Se construye desde `./frontend-reactjs` pasando `REACT_APP_API_BASE_URL` como build arg.
- Mapea el puerto `3000` del host al `80` interno de Nginx.
- Espera a que el backend esté healthy antes de iniciar.

### Red y volúmenes

- Docker Compose crea automáticamente una red interna donde los servicios se comunican por nombre (`db`, `backend`, `frontend`).
- El volumen `pgdata` persiste los datos de PostgreSQL.

---

## Instrucciones de uso

### Requisitos previos

- Docker Desktop instalado y en ejecución.
- Puertos libres: `3000`, `8080`, `5432`.

### Levantar la aplicación

```bash
# Desde la raíz del repositorio
docker compose up -d --build
```

### Verificar el despliegue

```bash
docker compose ps
curl http://localhost:8080/projects/all
curl http://localhost:3000/
```

### Detener la aplicación

```bash
docker compose down        # Detiene contenedores
docker compose down -v     # Detiene y elimina volúmenes (borra datos)
```

---

## Evidencias

Las capturas y salidas de terminal se encuentran en `docs/docker/evidencias/`. Ver el índice en [`evidencias.md`](./evidencias.md).
