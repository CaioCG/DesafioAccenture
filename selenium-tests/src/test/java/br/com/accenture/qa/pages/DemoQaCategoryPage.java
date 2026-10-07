package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

abstract class DemoQaCategoryPage extends BasePage {
    protected DemoQaCategoryPage(WebDriver driver) {
        super(driver);
    }

    protected void clickMenuItem(String title, String route) {
        By item = By.xpath("//li[.//span[normalize-space()='" + title + "']]");
        click(item);
        waitUntil(ExpectedConditions.urlContains(route));
    }
}
