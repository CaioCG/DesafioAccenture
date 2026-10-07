package br.com.accenture.qa.pages;

import org.openqa.selenium.WebDriver;

public class DemoQaWidgetsPage extends DemoQaCategoryPage {
    public DemoQaWidgetsPage(WebDriver driver) {
        super(driver);
    }

    public void clickProgressBar() {
        clickMenuItem("Progress Bar", "/progress-bar");
    }
}
