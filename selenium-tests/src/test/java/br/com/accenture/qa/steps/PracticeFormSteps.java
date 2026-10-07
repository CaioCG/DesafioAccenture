package br.com.accenture.qa.steps;

import br.com.accenture.qa.context.ScenarioState;
import br.com.accenture.qa.testdata.TestDataFactory;
import br.com.accenture.qa.testdata.model.PracticeFormData;
import br.com.accenture.qa.pages.Pages;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PracticeFormSteps {
    private final ScenarioState state;
    private final Pages pages;

    public PracticeFormSteps(ScenarioState state, Pages pages) {
        this.state = state;
        this.pages = pages;
    }

    @Given("the student opens the Practice Form page")
    public void openPracticeForm() {
        pages.home().navigate().clickForms();
        pages.forms().clickPracticeForm();
    }

    @When("the student fills in all fields with valid data and submits the form")
    public void completeAndSubmitForm() {
        PracticeFormData data = TestDataFactory.practiceForm();
        state.setFormValues(Map.of(
                "Student Name", data.firstName() + " " + data.lastName(),
                "Student Email", data.email(),
                "Gender", data.gender(),
                "Mobile", data.mobile(),
                "Date of Birth", data.dateOfBirth().format(DateTimeFormatter.ofPattern("dd MMMM,yyyy", Locale.ENGLISH)),
                "Subjects", data.subject(),
                "Hobbies", String.join(", ", data.hobbies()),
                "Picture", data.pictureName(),
                "Address", data.address(),
                "State and City", data.state() + " " + data.city()));

        pages.practiceForm()
                .submit(data, TestDataFactory.uploadPicturePath());
    }

    @Then("the confirmation modal displays all submitted values")
    public void modalConfirmsValues() {
        assertEquals("Thanks for submitting the form", pages.practiceForm().confirmationTitle());
        Map<String, String> actual = pages.practiceForm().confirmationDetails();
        state.getFormValues().forEach((label, expected) ->
                assertEquals(expected, actual.get(label), "Unexpected value for " + label + "; received summary: " + actual));
    }

    @When("the student closes the confirmation modal")
    public void closeModal() {
        pages.practiceForm().closeConfirmation();
    }

    @Then("the confirmation modal is no longer displayed")
    public void modalIsClosed() {
        assertFalse(pages.practiceForm().isConfirmationModalOpen());
    }
}
