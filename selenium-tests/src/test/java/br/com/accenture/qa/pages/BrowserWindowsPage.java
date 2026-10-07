package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BrowserWindowsPage extends BasePage {
    private static final By NEW_WINDOW_BUTTON = By.id("windowButton");

    public BrowserWindowsPage(WebDriver driver) {
        super(driver);
    }

    public BrowserWindowsPage clickNewWindowButton() {
        click(NEW_WINDOW_BUTTON);
        waitUntil(d -> d.getWindowHandles().size() > 1);
        return this;
    }
}
