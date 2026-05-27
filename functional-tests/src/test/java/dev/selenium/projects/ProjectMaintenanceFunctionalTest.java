package dev.selenium.projects;

import dev.selenium.support.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas funcionales de consulta y mantenimiento de proyectos (Integrante 5).
 */
class ProjectMaintenanceFunctionalTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        driver = WebDriverFactory.createChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void shouldDisplayProjectsListPage() {
        openProjectsPage();

        assertEquals("Projects", driver.findElement(By.tagName("h2")).getText());
        assertTrue(driver.findElement(By.cssSelector("table.project-table")).isDisplayed());
        assertTrue(isColumnVisible("ID"));
        assertTrue(isColumnVisible("Name"));
        assertTrue(isColumnVisible("Description"));
        assertTrue(isColumnVisible("Actions"));
    }

    @Test
    void shouldNavigateToProjectTasksFromList() {
        String projectName = "Consulta Selenium " + UUID.randomUUID();
        createProjectViaUi(projectName, "Proyecto para consulta funcional");

        clickProjectName(projectName);

        wait.until(ExpectedConditions.urlContains("/projects/"));
        wait.until(ExpectedConditions.urlContains("/tasks"));
        assertEquals("Project Tasks", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    void shouldOpenEditFormForProject() {
        String projectName = "Editar Selenium " + UUID.randomUUID();
        createProjectViaUi(projectName, "Proyecto editable");

        clickEditForProject(projectName);

        assertEquals("Edit Project", driver.findElement(By.tagName("h3")).getText());
        assertEquals(projectName, driver.findElement(By.cssSelector("[placeholder='Project Name']")).getAttribute("value"));
    }

    @Test
    void shouldUpdateProjectFromList() {
        String originalName = "Original " + UUID.randomUUID();
        String updatedName = "Actualizado " + UUID.randomUUID();
        createProjectViaUi(originalName, "Descripción original");

        clickEditForProject(originalName);

        WebElement nameInput = driver.findElement(By.cssSelector("[placeholder='Project Name']"));
        nameInput.clear();
        nameInput.sendKeys(updatedName);

        WebElement descriptionInput = driver.findElement(By.cssSelector("[placeholder='Project Description']"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Descripción actualizada");

        driver.findElement(By.xpath("//button[@type='submit' and text()='Update']")).click();

        waitForProjectInTable(updatedName);
        assertFalse(isProjectVisible(originalName));
        assertTrue(isProjectVisible(updatedName));
    }

    @Test
    void shouldDeleteProjectAfterConfirmation() {
        String projectName = "Eliminar Selenium " + UUID.randomUUID();
        createProjectViaUi(projectName, "Proyecto a eliminar");

        clickDeleteForProject(projectName);

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h3"), "Confirm Deletion"));
        driver.findElement(By.cssSelector("button.btn-delete")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//td[contains(text(), '" + projectName + "')]")
        ));
        assertFalse(isProjectVisible(projectName));
    }

    @Test
    void shouldCancelProjectDeletion() {
        String projectName = "Cancelar Selenium " + UUID.randomUUID();
        createProjectViaUi(projectName, "Proyecto no eliminado");

        clickDeleteForProject(projectName);

        driver.findElement(By.cssSelector("button.btn-cancel")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-box")));
        assertTrue(isProjectVisible(projectName));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void openProjectsPage() {
        driver.get(WebDriverFactory.baseUrl() + "/");

        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("table.project-table")),
                ExpectedConditions.textToBePresentInElementLocated(By.tagName("body"), "No projects found.")
        ));
    }

    private void createProjectViaUi(String name, String description) {
        openProjectsPage();
        driver.findElement(By.xpath("//button[contains(., '+ Add Project')]")).click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h3"), "Create Project"));

        driver.findElement(By.cssSelector("[placeholder='Project Name']")).sendKeys(name);
        driver.findElement(By.cssSelector("[placeholder='Project Description']")).sendKeys(description);
        driver.findElement(By.xpath("//button[@type='submit' and text()='Create']")).click();

        waitForProjectInTable(name);
    }

    private void waitForProjectInTable(String projectName) {
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//td[contains(text(), '" + projectName + "')]")
        ));
    }

    private boolean isProjectVisible(String projectName) {
        return !driver.findElements(By.xpath("//td[contains(text(), '" + projectName + "')]")).isEmpty();
    }

    private boolean isColumnVisible(String columnName) {
        return !driver.findElements(By.xpath("//th[text()='" + columnName + "']")).isEmpty();
    }

    private void clickProjectName(String projectName) {
        driver.findElement(By.xpath("//td[contains(text(), '" + projectName + "')]")).click();
    }

    private void clickEditForProject(String projectName) {
        findProjectRow(projectName).findElement(By.cssSelector("button[title='Edit']")).click();
    }

    private void clickDeleteForProject(String projectName) {
        findProjectRow(projectName).findElement(By.cssSelector("button[title='Delete']")).click();
    }

    private WebElement findProjectRow(String projectName) {
        return driver.findElement(By.xpath("//td[contains(text(), '" + projectName + "')]/parent::tr"));
    }
}
