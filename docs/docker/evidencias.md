# Evidencias — Docker y despliegue

Índice de evidencias del módulo de containerización.

| ID | Evidencia | Descripción | Archivo |
|----|-----------|-------------|---------|
| 1 | Build de imágenes | Salida de `docker compose build` | [01-docker-compose-build.txt](./evidencias/01-docker-compose-build.txt) |
| 2 | Contenedores en ejecución | Salida de `docker compose ps` e imágenes creadas | [02-docker-compose-ps.txt](./evidencias/02-docker-compose-ps.txt) |
| 3 | Verificación de endpoints | HTTP status y prueba CRUD vía API | [03-verificacion-endpoints.txt](./evidencias/03-verificacion-endpoints.txt) |
| 4 | Logs del backend | Arranque exitoso de Spring Boot en contenedor | [04-docker-logs-backend.txt](./evidencias/04-docker-logs-backend.txt) |
| 5 | Frontend en Docker | Aplicación React accesible en `localhost:3000` | [05-frontend-docker.png](./evidencias/05-frontend-docker.png) |
| 6 | Swagger en Docker | Documentación OpenAPI en `localhost:8080` | [06-swagger-docker.png](./evidencias/06-swagger-docker.png) |

## Archivos Docker del proyecto

| Archivo | Ubicación en el repositorio |
|---------|---------------------------|
| Dockerfile backend | `backend-springboot/Dockerfile` |
| Dockerfile frontend | `frontend-reactjs/Dockerfile` |
| Configuración Nginx | `frontend-reactjs/nginx.conf` |
| Orquestación | `docker-compose.yml` |
| Documentación | `docs/docker/docker-despliegue.md` |

## Comandos para reproducir las evidencias

```bash
# Construir y levantar
docker compose up -d --build

# Estado de contenedores
docker compose ps

# Verificar API
curl http://localhost:8080/projects/all

# Ver logs del backend
docker compose logs backend --tail 20
```
