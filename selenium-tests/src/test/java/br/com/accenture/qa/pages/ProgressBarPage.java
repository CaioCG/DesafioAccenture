package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProgressBarPage extends BasePage {
    private static final By PROGRESS_BAR = By.cssSelector("#progressBar .progress-bar");
    private static final By START_STOP_BUTTON = By.id("startStopButton");
    private static final By RESET_BUTTON = By.id("resetButton");

    public ProgressBarPage(WebDriver driver) {
        super(driver);
    }

    public ProgressBarPage startAndStopBelow(int percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("The percentage must be between 0 and 100.");
        }

        click(START_STOP_BUTTON);
        WebDriverWait rapidWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        rapidWait.pollingEvery(Duration.ofMillis(15));
        rapidWait.until(d -> {
            int value = progressValue();
            return value >= Math.max(0, percentage - 5) && value <= percentage;
        });
        if (buttonText(START_STOP_BUTTON).equalsIgnoreCase("Stop")) {
            click(START_STOP_BUTTON);
            waitUntil(d -> buttonText(START_STOP_BUTTON).equalsIgnoreCase("Start"));
        }
        return this;
    }

    public ProgressBarPage restartToCompletion() {
        if (progressValue() < 100) {
            click(START_STOP_BUTTON);
        }
        waitUntil(d -> progressValue() == 100);
        return this;
    }

    public ProgressBarPage reset() {
        click(RESET_BUTTON);
        WebDriverWait resetWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        resetWait.pollingEvery(Duration.ofMillis(100));
        try {
            resetWait.until(d -> progressValue() == 0);
        } catch (org.openqa.selenium.TimeoutException ignored) {
            click(RESET_BUTTON);
            waitUntil(d -> progressValue() == 0);
        }
        return this;
    }

    public int progressValue() {
        WebElement bar = waitUntilVisible(PROGRESS_BAR);
        String value = bar.getAttribute("aria-valuenow");
        if (value == null || value.isBlank()) {
            value = bar.getText().replace("%", "").trim();
        }
        return Integer.parseInt(value);
    }

    private String buttonText(By locator) {
        return waitUntilVisible(locator).getText().trim();
    }
}
