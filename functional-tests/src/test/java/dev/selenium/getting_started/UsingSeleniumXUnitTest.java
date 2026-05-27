package dev.selenium.getting_started;

import dev.selenium.support.WebDriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas funcionales de navegación inicial sobre Task Manager.
 */
class UsingSeleniumXUnitTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setup() {
        driver = WebDriverFactory.createChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void shouldLoadProjectsPage() {
        driver.get(WebDriverFactory.baseUrl() + "/");

        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName("h2"), "Projects"));
        assertEquals("Projects", driver.findElement(By.tagName("h2")).getText());
    }

    @Test
    void shouldShowAddProjectActionOnProjectsPage() {
        driver.get(WebDriverFactory.baseUrl() + "/");

        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[contains(., '+ Add Project')]")
        ));
        assertTrue(driver.findElement(By.xpath("//button[contains(., '+ Add Project')]")).isDisplayed());
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
