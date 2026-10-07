package br.com.accenture.qa.api.clients;

import br.com.accenture.qa.api.DemoQaConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.with;

public class AccountClient {
    private final RequestSpecification baseRequest = new RequestSpecBuilder()
            .setBaseUri(DemoQaConfig.apiBaseUrl())
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

    public Response createUser(String username, String password) {
        return with().spec(baseRequest)
                .body(Map.of("userName", username, "password", password))
                .when().post("/Account/v1/User");
    }

    public Response generateToken(String username, String password) {
        return with().spec(baseRequest)
                .body(Map.of("userName", username, "password", password))
                .when().post("/Account/v1/GenerateToken");
    }

    public Response isAuthorized(String username, String password) {
        return with().spec(baseRequest)
                .body(Map.of("userName", username, "password", password))
                .when().post("/Account/v1/Authorized");
    }

    public Response getUser(String userId, String token) {
        return with().spec(baseRequest)
                .header("Authorization", "Bearer " + token)
                .when().get("/Account/v1/User/{userId}", userId);
    }

    public Response deleteUser(String userId, String token) {
        return with().spec(baseRequest)
                .header("Authorization", "Bearer " + token)
                .when().delete("/Account/v1/User/{userId}", userId);
    }
}
