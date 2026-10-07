package br.com.accenture.qa.hooks;

import br.com.accenture.qa.config.DriverFactory;
import br.com.accenture.qa.context.ScenarioState;
import br.com.accenture.qa.pages.Pages;
import br.com.accenture.qa.pages.WebTablesPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.charset.StandardCharsets;

public class UiHooks {
    private final ScenarioState state;
    private final Pages pages;

    public UiHooks(ScenarioState state, Pages pages) {
        this.state = state;
        this.pages = pages;
    }

    @Before
    public void startBrowser() {
        state.setDriver(DriverFactory.createChrome());
    }

    @After
    public void finishBrowser(Scenario scenario) {
        WebDriver driver = state.getDriver();
        if (driver == null) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                scenario.attach(((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES),
                        "image/png", "ui-failure.png");
            }
            cleanupCreatedRecords(scenario);
        } finally {
            try {
                driver.quit();
            } finally {
                state.setDriver(null);
            }
        }
    }

    private void cleanupCreatedRecords(Scenario scenario) {
        if (state.getCreatedRecords().isEmpty()) {
            return;
        }
        try {
            pages.home().navigate().clickElements();
            pages.elements().clickWebTables();
            WebTablesPage page = pages.webTables();
            for (var record : state.getCreatedRecords()) {
                page.deleteIfPresent(record.email());
            }
        } catch (RuntimeException cleanupFailure) {
            String details = "Failed to clean up test records: " + cleanupFailure;
            scenario.attach(details.getBytes(StandardCharsets.UTF_8), "text/plain", "ui-cleanup.txt");
            throw new IllegalStateException("Failed to clean up records created by the scenario.", cleanupFailure);
        }
    }
}
