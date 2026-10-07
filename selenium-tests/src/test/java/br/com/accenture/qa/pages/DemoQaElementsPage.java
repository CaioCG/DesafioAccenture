package br.com.accenture.qa.pages;

import org.openqa.selenium.WebDriver;

public class DemoQaElementsPage extends DemoQaCategoryPage {
    public DemoQaElementsPage(WebDriver driver) {
        super(driver);
    }

    public void clickWebTables() {
        clickMenuItem("Web Tables", "/webtables");
    }
}
