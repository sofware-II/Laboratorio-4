# Evidencias de Validación del Backend (Punto 3)

Este directorio está destinado a almacenar las capturas de pantalla que acreditan la correcta ejecución, validación y documentación de la API del módulo backend (`backend-springboot`).

---

## Catálogo de Capturas Requeridas

A continuación se describen los archivos visuales (`.png`) que deben ubicarse en esta carpeta y qué información o proceso representa cada uno de ellos:

### 1. `mvn-test.png`
- **Descripción:** Captura de pantalla de la consola o terminal tras ejecutar exitosamente la suite de pruebas unitarias y de integración del backend con el comando:
  ```powershell
  cd backend-springboot
  .\mvnw.cmd test
  ```
- **Contenido visual esperado:** La imagen debe mostrar el reporte final de Maven indicando la ejecución satisfactoria de las 42 pruebas (`Tests run: 42, Failures: 0, Errors: 0`), la generación del reporte de cobertura de JaCoCo y el banner verde o texto confirmando **`[INFO] BUILD SUCCESS`**.

---

### 2. `mvn-clean-install.png`
- **Descripción:** Captura de pantalla de la terminal mostrando la compilación, limpieza y construcción completa del artefacto de la aplicación mediante el comando:
  ```powershell
  cd backend-springboot
  .\mvnw.cmd clean install
  ```
- **Contenido visual esperado:** La imagen debe evidenciar la limpieza del directorio `target/`, la autogeneración exitosa de los mapeadores de MapStruct y constructores de Lombok, la validación de las pruebas unitarias y el mensaje final indicando el empaquetado del archivo `taskManager-0.0.1-SNAPSHOT.jar` con el estado **`[INFO] BUILD SUCCESS`**.

---

### 3. `swagger-ui.png`
- **Descripción:** Captura de pantalla desde el navegador web mostrando la interfaz gráfica de la documentación OpenAPI/Swagger en ejecución.
- **Cómo capturarla:** 
  1. Inicie el servidor localmente ejecutando `.\mvnw.cmd spring-boot:run` dentro de `backend-springboot/`.
  2. Abra su navegador web y diríjase a la URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).
  3. Tome una captura de la interfaz donde se aprecie claramente el título del proyecto (**taskManager API** / **OpenAPI definition**) y las secciones de los controladores para la gestión de tareas (`task-controller` o `/tasks`) y proyectos (`project-controller` o `/projects`).
- **Contenido visual esperado:** La interfaz interactiva desplegada con los endpoints REST documentados (`POST`, `GET`, `PUT`, `DELETE`).

---

## Nota sobre Placeholders de Evidencia

Actualmente, los archivos referenciados en la documentación técnica (`mvn-test.png`, `mvn-clean-install.png` y `swagger-ui.png`) se encuentran configurados y documentados como **placeholders** en la estructura del repositorio para garantizar la integridad y coherencia de los enlaces en los documentos Markdown. 

Para completar formalmente las evidencias, se deben seguir las instrucciones descritas en la sección anterior, ejecutar los comandos correspondientes en su entorno local, tomar las capturas de pantalla de los resultados y **reemplazar o añadir los archivos `.png` con los nombres exactos indicados en esta misma carpeta** (`docs/testing/evidencias/`).
