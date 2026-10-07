package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.function.Function;
import java.util.logging.Logger;

public abstract class BasePage {
    private static final Logger LOGGER = Logger.getLogger(BasePage.class.getName());
    private static final Duration WAIT_POLLING_INTERVAL = Duration.ofMillis(120);

    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.wait.pollingEvery(WAIT_POLLING_INTERVAL);
    }

    protected WebElement waitUntilVisible(By locator) {
        return waitUntil(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return waitUntil(ExpectedConditions.elementToBeClickable(locator));
    }

    protected <T> T waitUntil(Function<WebDriver, T> condition) {
        return wait.until(condition);
    }

    protected <T> T waitUntil(ExpectedCondition<T> condition, Duration timeout) {
        return new WebDriverWait(driver, timeout)
                .pollingEvery(WAIT_POLLING_INTERVAL)
                .until(condition);
    }

    protected void click(By locator) {
        WebElement element = clickable(locator);
        try {
            element.click();
        } catch (ElementClickInterceptedException ignored) {
            LOGGER.warning("Click intercepted on " + getClass().getSimpleName()
                    + " for element " + locator + "; using JavaScript as a fallback.");
            clickWithJavascript(clickable(locator));
        }
    }

    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String text(By locator) {
        return waitUntilVisible(locator).getText().trim();
    }

    private void clickWithJavascript(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }
}
