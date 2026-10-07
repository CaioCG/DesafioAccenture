package br.com.accenture.qa.pages;

import br.com.accenture.qa.config.DemoQaConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DemoQaHome extends BasePage {
    public DemoQaHome(WebDriver driver) {
        super(driver);
    }

    public DemoQaHome navigate() {
        driver.get(DemoQaConfig.uiBaseUrl());
        waitUntilVisible(By.cssSelector(".top-card"));
        return this;
    }

    public void clickElements() {
        clickCategory("Elements", "/elements");
    }

    public void clickForms() {
        clickCategory("Forms", "/forms");
    }

    public void clickAlertsFrameWindows() {
        clickCategory("Alerts, Frame & Windows", "/alertsWindows");
    }

    public void clickWidgets() {
        clickCategory("Widgets", "/widgets");
    }

    public void clickInteractions() {
        clickCategory("Interactions", "/interaction");
    }

    public void clickBookStoreApplication() {
        clickCategory("Book Store Application", "/books");
    }

    private void clickCategory(String title, String route) {
        By card = By.xpath("//div[contains(concat(' ', normalize-space(@class), ' '), ' top-card ')][.//h5[normalize-space()='" + title + "']]");
        click(card);
        waitUntil(ExpectedConditions.urlContains(route));
    }
}
