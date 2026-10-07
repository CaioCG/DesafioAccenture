package br.com.accenture.qa.steps;

import br.com.accenture.qa.api.clients.AccountClient;
import br.com.accenture.qa.context.ScenarioState;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiHooks {
    private final ScenarioState state;
    private final AccountClient accountClient = new AccountClient();

    public ApiHooks(ScenarioState state) {
        this.state = state;
    }

    @After
    public void deleteCreatedTestUser(Scenario scenario) {
        if (state.getUserId() == null) {
            return;
        }
        if (state.getToken() == null && state.getUsername() != null && state.getPassword() != null) {
            Response tokenResponse = accountClient.generateToken(state.getUsername(), state.getPassword());
            if (tokenResponse.statusCode() == 200) {
                state.setToken(tokenResponse.jsonPath().getString("token"));
            }
        }
        if (state.getToken() == null || state.getToken().isBlank()) {
            scenario.attach("The test user was created, but a token could not be retrieved to delete it."
                    .getBytes(java.nio.charset.StandardCharsets.UTF_8), "text/plain", "api-cleanup.txt");
            return;
        }
        Response cleanup = accountClient.deleteUser(state.getUserId(), state.getToken());
        assertTrue(cleanup.statusCode() == 200 || cleanup.statusCode() == 204,
                "Failed to delete the test user: " + cleanup.statusCode() + " " + cleanup.asString());
    }
}
