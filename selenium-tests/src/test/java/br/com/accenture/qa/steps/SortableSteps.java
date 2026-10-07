package br.com.accenture.qa.steps;

import br.com.accenture.qa.pages.Pages;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortableSteps {
    private final Pages pages;

    public SortableSteps(Pages pages) {
        this.pages = pages;
    }

    @Given("the user opens the Sortable page from the Interactions menu")
    public void openSortable() {
        pages.home().navigate().clickInteractions();
        pages.interactions().clickSortable();
    }

    @When("the user sorts the items using drag and drop")
    public void sortItems() {
        pages.sortable().arrangeAscending();
    }

    @Then("the user sees the items in ascending order from One to Six")
    public void verifyAscending() {
        assertEquals(pages.sortable().ascendingOrder(), pages.sortable().getItemTexts());
    }
}
