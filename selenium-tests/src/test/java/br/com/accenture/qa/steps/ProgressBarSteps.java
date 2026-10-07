package br.com.accenture.qa.steps;

import br.com.accenture.qa.pages.Pages;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProgressBarSteps {
    private final Pages pages;

    public ProgressBarSteps(Pages pages) {
        this.pages = pages;
    }

    @Given("the user accesses the Progress Bar page from the Widgets menu")
    public void openProgressBar() {
        pages.home().navigate().clickWidgets();
        pages.widgets().clickProgressBar();
    }

    @When("the user starts the progress bar and stops at or below {int} percent")
    public void stopBelowPercentage(int percentage) {
        pages.progressBar().startAndStopBelow(percentage);
    }

    @Then("the progress value is at or below {int} percent")
    public void stoppedAtOrBelowPercentage(int percentage) {
        int value = pages.progressBar().progressValue();
        assertTrue(value >= 0 && value <= percentage,
                "Expected progress to be between 0 and " + percentage + " percent, but was " + value + ".");
    }

    @When("the user restarts the progress bar and waits for completion")
    public void restartProgress() {
        pages.progressBar().restartToCompletion();
    }

    @Then("the progress bar reaches 100 percent")
    public void progressCompletes() {
        assertEquals(100, pages.progressBar().progressValue(), "The progress bar did not reach 100 percent.");
    }

    @When("the user resets the progress bar")
    public void resetProgress() {
        pages.progressBar().reset();
    }

    @Then("the progress bar returns to zero percent")
    public void progressResets() {
        assertEquals(0, pages.progressBar().progressValue(), "The progress bar did not return to zero percent.");
    }
}
