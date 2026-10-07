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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountSteps {
    private final ScenarioState state;
    private final AccountClient accountClient = new AccountClient();
    private final BookStoreClient bookStoreClient = new BookStoreClient();
    private Response response;

    public AccountSteps(ScenarioState state) {
        this.state = state;
    }

    @Given("the user has unique account credentials")
    public void prepareCredentials() {
        AccountScenarioSetup.generateCredentials(state);
    }

    @Given("a user account exists with unique credentials")
    public void createTestUserForScenario() {
        AccountScenarioSetup.createAccount(state, accountClient);
    }

    @Given("a user account exists with an access token")
    public void createTestUserWithToken() {
        AccountScenarioSetup.createAccount(state, accountClient);
        AccountScenarioSetup.generateAccessToken(state, accountClient);
    }

    @Given("a user account has two reserved books")
    public void prepareUserWithReservedBooks() {
        AccountScenarioSetup.createAccount(state, accountClient);
        AccountScenarioSetup.generateAccessToken(state, accountClient);

        Response booksResponse = bookStoreClient.listBooks();
        assertEquals(200, booksResponse.statusCode(), booksResponse.asString());

        state.setSelectedIsbns(BookStoreScenarioData.firstTwoAvailableIsbns(booksResponse));

        Response reservationResponse = bookStoreClient.addBooks(
                state.getUserId(), state.getSelectedIsbns(), state.getToken());
        assertEquals(201, reservationResponse.statusCode(), reservationResponse.asString());

        List<String> reservedIsbns = BookStoreScenarioData.isbns(reservationResponse);
        assertTrue(reservedIsbns.containsAll(state.getSelectedIsbns()),
                "Expected ISBNs " + state.getSelectedIsbns() + ", received " + reservedIsbns);
    }

    @When("the user creates an account with those credentials")
    public void createUser() {
        response = accountClient.createUser(state.getUsername(), state.getPassword());
    }

    @Then("the account is created and its identifier is returned")
    public void userCreated() {
        assertEquals(201, response.statusCode(), response.asString());

        String userId = response.jsonPath().getString("userID");
        assertNotNull(userId, response.asString());
        assertFalse(userId.isBlank());
        state.setUserId(userId);
    }

    @When("the user requests an access token")
    public void generateToken() {
        response = accountClient.generateToken(state.getUsername(), state.getPassword());
    }

    @Then("an access token is returned")
    public void tokenGenerated() {
        assertEquals(200, response.statusCode(), response.asString());

        String token = response.jsonPath().getString("token");
        assertNotNull(token, response.asString());
        assertFalse(token.isBlank());
        state.setToken(token);
    }

    @When("the user checks authorization")
    public void authorizeUser() {
        response = accountClient.isAuthorized(state.getUsername(), state.getPassword());
    }

    @Then("the API confirms the user is authorized")
    public void userIsAuthorized() {
        assertEquals(200, response.statusCode(), response.asString());
        assertEquals(Boolean.TRUE, response.as(Boolean.class));
    }

    @When("the user requests account details")
    public void getUserDetails() {
        response = accountClient.getUser(state.getUserId(), state.getToken());
    }

    @Then("the account details include the user and both reserved books")
    public void userDetailsContainReservations() {
        assertEquals(200, response.statusCode(), response.asString());
        assertEquals(state.getUserId(), response.jsonPath().getString("userId"));
        assertEquals(state.getUsername(), response.jsonPath().getString("username"));
        List<String> returnedIsbns = BookStoreScenarioData.isbns(response);
        assertTrue(returnedIsbns.containsAll(state.getSelectedIsbns()),
                "Expected ISBNs " + state.getSelectedIsbns() + ", received " + returnedIsbns);
    }
}
