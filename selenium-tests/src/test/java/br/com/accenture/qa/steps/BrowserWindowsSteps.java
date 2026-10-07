package br.com.accenture.qa.steps;

import br.com.accenture.qa.context.ScenarioState;
import br.com.accenture.qa.pages.Pages;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BrowserWindowsSteps {
    private final ScenarioState state;
    private final Pages pages;

    public BrowserWindowsSteps(ScenarioState state, Pages pages) {
        this.state = state;
        this.pages = pages;
    }

    @Given("the user opens the Browser Windows page from the Alerts, Frame & Windows menu")
    public void openBrowserWindows() {
        pages.home()
                .navigate()
                .clickAlertsFrameWindows();
        pages.alertsFrameWindows()
                .clickBrowserWindows();
        state.setParentWindow(state.getDriver().getWindowHandle());
    }

    @When("the user clicks the New Window button")
    public void clickNewWindowButton() {
        pages.browserWindows()
                .clickNewWindowButton();
        pages.sample()
                .switchToNewWindow(state.getParentWindow())
                .waitUntilLoaded();

        state.setChildWindow(state.getDriver().getWindowHandle());
    }

    @Then("the sample page displays the message {string}")
    public void verifySampleMessage(String expected) {
        assertNotEquals(state.getParentWindow(), state.getChildWindow(),
                "The sample page should open in a separate window.");
        assertEquals(expected, pages.sample().message(),
                "The sample page displayed an unexpected message.");
    }

    @When("the user closes the sample window")
    public void closeNewWindow() {
        pages.sample().closeWindow();
        state.getDriver().switchTo().window(state.getParentWindow());
    }

    @Then("the user returns to the original window")
    public void verifyOriginalWindow() {
        assertEquals(state.getParentWindow(), state.getDriver().getWindowHandle(),
                "The browser should return to the original window.");
        assertEquals(1, state.getDriver().getWindowHandles().size(),
                "Only the original browser window should remain open.");
    }
}
