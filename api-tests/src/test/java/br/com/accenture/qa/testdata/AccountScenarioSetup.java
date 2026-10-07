package br.com.accenture.qa.testdata;

import br.com.accenture.qa.api.clients.AccountClient;
import br.com.accenture.qa.context.ScenarioState;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class AccountScenarioSetup {
    private AccountScenarioSetup() {
    }

    public static void generateCredentials(ScenarioState state) {
        state.setUsername(AccountCredentialsGenerator.username());
        state.setPassword(AccountCredentialsGenerator.password());
    }

    public static void createAccount(ScenarioState state, AccountClient accountClient) {
        generateCredentials(state);
        Response response = accountClient.createUser(state.getUsername(), state.getPassword());
        assertEquals(201, response.statusCode(), response.asString());

        // DemoQA returns `userID` although its published Swagger contract documents `userId`.
        String userId = response.jsonPath().getString("userID");
        assertNotNull(userId, response.asString());
        assertFalse(userId.isBlank());
        state.setUserId(userId);
    }

    public static void generateAccessToken(ScenarioState state, AccountClient accountClient) {
        Response response = accountClient.generateToken(state.getUsername(), state.getPassword());
        assertEquals(200, response.statusCode(), response.asString());

        String token = response.jsonPath().getString("token");
        assertNotNull(token, response.asString());
        assertFalse(token.isBlank());
        state.setToken(token);
    }
}
