package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class SamplePage extends BasePage {
    private static final By SAMPLE_HEADING = By.id("sampleHeading");

    public SamplePage(WebDriver driver) {
        super(driver);
    }

    public SamplePage switchToNewWindow(String parentHandle) {
        String sampleWindow = driver.getWindowHandles().stream()
                .filter(handle -> !handle.equals(parentHandle))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("The Sample window was not opened."));
        driver.switchTo().window(sampleWindow);
        return this;
    }

    public SamplePage waitUntilLoaded() {
        waitUntil(
                ExpectedConditions.visibilityOfElementLocated(SAMPLE_HEADING),
                Duration.ofSeconds(15));
        return this;
    }

    public String message() {
        return text(SAMPLE_HEADING);
    }

    public void closeWindow() {
        driver.close();
        waitUntil(ExpectedConditions.numberOfWindowsToBe(1));
    }
}
