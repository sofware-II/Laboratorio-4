package com.hasbi.taskManager.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Selenium End-to-End Tests para Actualización de Tareas
 * 
 * Pruebas funcionales automatizadas usando Selenium WebDriver
 * Valida la interfaz de usuario en el navegador
 * 
 * Prerequisitos:
 * - ChromeDriver en PATH o WebDriverManager (incluido en pom.xml)
 * - Aplicación ejecutándose en http://localhost:3000 (React frontend)
 * - API ejecutándose en http://localhost:8080 (Spring Boot backend)
 * 
 * @author Task Manager Selenium Test Suite
 * @version 1.0
 */
@DisplayName("Task Update End-to-End Tests with Selenium")
public class TaskUpdateSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:3000";
    private static final int TIMEOUT_SECONDS = 10;

    // ========== SetUp ==========
    @BeforeEach
    void setUp() {
        // Descargar ChromeDriver automáticamente
        WebDriverManager.chromedriver().setup();
        
        // Inicializar Chrome WebDriver
        driver = new ChromeDriver();
        
        // Configurar espera implícita y explícita
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT_SECONDS));
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
    }

    // ========== TC01: Actualizar Título desde UI ==========
    @Test
    @DisplayName("TC01-UI: Navegar a Tasks, abrir tarea y actualizar título")
    void testUpdateTaskTitle_FromUI() {
        try {
            // Arrange - Navegar a la aplicación
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Verificar que estamos en la página de tareas
            String currentUrl = driver.getCurrentUrl();
            assertTrue(currentUrl.contains("tasks") || currentUrl.contains("localhost:3000"), 
                "Debería estar en la página de tareas");

            // Act - Buscar y hacer clic en una tarea existente
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            assertNotNull(firstTask, "Debería haber una tarea disponible");
            
            firstTask.click();
            Thread.sleep(1000);

            // Assert - Verificar que se abre el diálogo de edición
            WebElement titleInput = driver.findElement(By.id("task-title-input"));
            assertNotNull(titleInput, "Debería existir el campo de título");

            // Limpiar y actualizar título
            titleInput.clear();
            titleInput.sendKeys("Título Actualizado desde Selenium");

            // Guardar cambios
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Verificar que la tarea fue actualizada
            WebElement updatedTitle = driver.findElement(By.css("[data-testid='task-title']"));
            assertEquals("Título Actualizado desde Selenium", updatedTitle.getText(),
                "El título debería haberse actualizado");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC02: Actualizar Descripción desde UI ==========
    @Test
    @DisplayName("TC02-UI: Actualizar descripción de una tarea")
    void testUpdateTaskDescription_FromUI() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Abrir primera tarea
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            // Localizar y actualizar descripción
            WebElement descriptionInput = driver.findElement(By.id("task-description-input"));
            descriptionInput.clear();
            descriptionInput.sendKeys("Nueva descripción desde Selenium");

            // Guardar
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert
            WebElement updatedDescription = driver.findElement(By.css("[data-testid='task-description']"));
            assertEquals("Nueva descripción desde Selenium", updatedDescription.getText(),
                "La descripción debería haberse actualizado");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC03: Cambiar Estado de Tarea desde UI ==========
    @Test
    @DisplayName("TC03-UI: Cambiar estado de TODO a IN_PROGRESS")
    void testUpdateTaskStatus_FromUI() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Abrir tarea
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            // Cambiar estado mediante dropdown/select
            WebElement statusSelect = driver.findElement(By.id("task-status-select"));
            Select select = new Select(statusSelect);
            select.selectByValue("IN_PROGRESS");

            // Guardar
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert
            WebElement taskStatus = driver.findElement(By.css("[data-testid='task-status']"));
            assertTrue(taskStatus.getText().contains("IN_PROGRESS"),
                "El estado debería ser IN_PROGRESS");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC04: Cambiar Prioridad desde UI ==========
    @Test
    @DisplayName("TC04-UI: Cambiar prioridad de MEDIUM a HIGH")
    void testUpdateTaskPriority_FromUI() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Abrir tarea
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            // Cambiar prioridad
            WebElement prioritySelect = driver.findElement(By.id("task-priority-select"));
            Select select = new Select(prioritySelect);
            select.selectByValue("HIGH");

            // Guardar
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert
            WebElement taskPriority = driver.findElement(By.css("[data-testid='task-priority']"));
            assertTrue(taskPriority.getText().contains("HIGH"),
                "La prioridad debería ser HIGH");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC05: Validar Campos Requeridos desde UI ==========
    @Test
    @DisplayName("TC05-UI: Intentar guardar sin título muestra error de validación")
    void testUpdateTask_MissingTitle_ShowsError() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Abrir tarea
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            // Limpiar título (hacerlo obligatorio)
            WebElement titleInput = driver.findElement(By.id("task-title-input"));
            titleInput.clear();

            // Intentar guardar
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert - Verificar mensaje de error
            WebElement errorMessage = driver.findElement(By.css("[data-testid='error-message']"));
            assertTrue(errorMessage.isDisplayed(),
                "Debería mostrar un mensaje de error");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC06: Cancelar Cambios desde UI ==========
    @Test
    @DisplayName("TC06-UI: Cancelar cambios no guarda los datos")
    void testUpdateTask_CancelChanges_DoesNotSave() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Obtener título original
            WebElement titleBefore = driver.findElement(By.css("[data-testid='task-title']"));
            String originalTitle = titleBefore.getText();

            // Act - Abrir tarea y hacer cambios
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            WebElement titleInput = driver.findElement(By.id("task-title-input"));
            titleInput.clear();
            titleInput.sendKeys("Título Temporal Que No Se Guardará");

            // Cancelar
            WebElement cancelButton = driver.findElement(By.id("task-cancel-button"));
            cancelButton.click();
            Thread.sleep(1000);

            // Assert - Verificar que el título no cambió
            WebElement titleAfter = driver.findElement(By.css("[data-testid='task-title']"));
            assertEquals(originalTitle, titleAfter.getText(),
                "El título no debería cambiar después de cancelar");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC07: Actualizar Múltiples Campos desde UI ==========
    @Test
    @DisplayName("TC07-UI: Actualizar título, descripción, estado y prioridad simultáneamente")
    void testUpdateTask_MultipleFields_FromUI() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Abrir tarea
            WebElement firstTask = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            firstTask.click();
            Thread.sleep(1000);

            // Actualizar múltiples campos
            WebElement titleInput = driver.findElement(By.id("task-title-input"));
            titleInput.clear();
            titleInput.sendKeys("Tarea Completamente Actualizada");

            WebElement descriptionInput = driver.findElement(By.id("task-description-input"));
            descriptionInput.clear();
            descriptionInput.sendKeys("Descripción completa y detallada");

            WebElement statusSelect = driver.findElement(By.id("task-status-select"));
            new Select(statusSelect).selectByValue("DONE");

            WebElement prioritySelect = driver.findElement(By.id("task-priority-select"));
            new Select(prioritySelect).selectByValue("LOW");

            // Guardar
            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert - Verificar todos los campos
            WebElement taskTitle = driver.findElement(By.css("[data-testid='task-title']"));
            WebElement taskDescription = driver.findElement(By.css("[data-testid='task-description']"));
            WebElement taskStatus = driver.findElement(By.css("[data-testid='task-status']"));
            WebElement taskPriority = driver.findElement(By.css("[data-testid='task-priority']"));

            assertEquals("Tarea Completamente Actualizada", taskTitle.getText(),
                "El título debería actualizarse");
            assertEquals("Descripción completa y detallada", taskDescription.getText(),
                "La descripción debería actualizarse");
            assertTrue(taskStatus.getText().contains("DONE"),
                "El estado debería ser DONE");
            assertTrue(taskPriority.getText().contains("LOW"),
                "La prioridad debería ser LOW");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TC08: Búsqueda y Actualización ==========
    @Test
    @DisplayName("TC08-UI: Buscar tarea y actualizarla")
    void testSearchAndUpdateTask_FromUI() {
        try {
            // Arrange
            driver.get(BASE_URL);
            Thread.sleep(2000);

            // Act - Buscar tarea
            WebElement searchInput = driver.findElement(By.id("task-search-input"));
            searchInput.sendKeys("Test");
            Thread.sleep(1000);

            // Seleccionar resultado
            WebElement taskItem = driver.findElement(By.css("[data-testid='task-item']:first-child"));
            taskItem.click();
            Thread.sleep(1000);

            // Actualizar
            WebElement titleInput = driver.findElement(By.id("task-title-input"));
            titleInput.clear();
            titleInput.sendKeys("Tarea Encontrada y Actualizada");

            WebElement saveButton = driver.findElement(By.id("task-save-button"));
            saveButton.click();
            Thread.sleep(1000);

            // Assert
            WebElement updatedTitle = driver.findElement(By.css("[data-testid='task-title']"));
            assertEquals("Tarea Encontrada y Actualizada", updatedTitle.getText(),
                "La tarea debería actualizarse después de buscar");

        } catch (Exception e) {
            fail("Prueba falló: " + e.getMessage());
        }
    }

    // ========== TearDown ==========
    @AfterEach
    void tearDown() {
        // Cerrar el navegador después de cada prueba
        if (driver != null) {
            driver.quit();
        }
    }
}
