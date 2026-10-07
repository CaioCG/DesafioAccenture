package br.com.accenture.qa.pages;

import org.openqa.selenium.WebDriver;

public class DemoQaInteractionsPage extends DemoQaCategoryPage {
    public DemoQaInteractionsPage(WebDriver driver) {
        super(driver);
    }

    public void clickSortable() {
        clickMenuItem("Sortable", "/sortable");
    }
}
