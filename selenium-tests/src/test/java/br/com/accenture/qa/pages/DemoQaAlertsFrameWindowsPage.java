package br.com.accenture.qa.pages;

import org.openqa.selenium.WebDriver;

public class DemoQaAlertsFrameWindowsPage extends DemoQaCategoryPage {
    public DemoQaAlertsFrameWindowsPage(WebDriver driver) {
        super(driver);
    }

    public void clickBrowserWindows() {
        clickMenuItem("Browser Windows", "/browser-windows");
    }
}
