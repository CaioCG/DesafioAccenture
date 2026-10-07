package br.com.accenture.qa.steps;

import br.com.accenture.qa.api.clients.AccountClient;
import br.com.accenture.qa.api.clients.BookStoreClient;
import br.com.accenture.qa.context.ScenarioState;
import br.com.accenture.qa.testdata.AccountScenarioSetup;
import br.com.accenture.qa.testdata.BookStoreScenarioData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookStoreSteps {
    private final ScenarioState state;
    private final AccountClient accountClient = new AccountClient();
    private final BookStoreClient bookStoreClient = new BookStoreClient();
    private Response response;

    public BookStoreSteps(ScenarioState state) {
        this.state = state;
    }

    @Given("the user has a valid access token and two available books are selected")
    public void prepareUserWithTokenAndAvailableBooks() {
        AccountScenarioSetup.createAccount(state, accountClient);
        AccountScenarioSetup.generateAccessToken(state, accountClient);

        Response booksResponse = bookStoreClient.listBooks();
        assertEquals(200, booksResponse.statusCode(), booksResponse.asString());

        state.setSelectedIsbns(BookStoreScenarioData.firstTwoAvailableIsbns(booksResponse));
    }

    @When("the user requests the available books")
    public void listAvailableBooks() {
        response = bookStoreClient.listBooks();
    }

    @Then("the catalog includes at least two books with ISBNs")
    public void booksAreAvailable() {
        assertEquals(200, response.statusCode(), response.asString());
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/bookstore-books-schema.json"));

        state.setSelectedIsbns(BookStoreScenarioData.firstTwoAvailableIsbns(response));
    }

    @When("the user adds the selected books to their collection")
    public void reserveBooks() {
        response = bookStoreClient.addBooks(
                state.getUserId(),
                state.getSelectedIsbns(),
                state.getToken()
        );
    }

    @Then("the user's collection includes both selected ISBNs")
    public void booksAreReserved() {
        assertEquals(201, response.statusCode(), response.asString());

        // The live endpoint returns the reserved collection as a `books` array.
        List<String> returnedIsbns = BookStoreScenarioData.isbns(response);
        assertTrue(returnedIsbns.containsAll(state.getSelectedIsbns()),
                "Expected ISBNs " + state.getSelectedIsbns() + ", received " + returnedIsbns);
    }
}
