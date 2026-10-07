package br.com.accenture.qa.pages;

import org.openqa.selenium.WebDriver;

public class DemoQaFormsPage extends DemoQaCategoryPage {
    public DemoQaFormsPage(WebDriver driver) {
        super(driver);
    }

    public void clickPracticeForm() {
        clickMenuItem("Practice Form", "/automation-practice-form");
    }
}
