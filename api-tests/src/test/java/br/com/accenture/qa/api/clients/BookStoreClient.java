package br.com.accenture.qa.api.clients;

import br.com.accenture.qa.api.DemoQaConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.with;

public class BookStoreClient {
    private final RequestSpecification baseRequest = new RequestSpecBuilder()
            .setBaseUri(DemoQaConfig.apiBaseUrl())
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();

    public Response listBooks() {
        return with()
                .spec(baseRequest)
                .when()
                .get("/BookStore/v1/Books");
    }

    public Response addBooks(String userId, List<String> isbns, String token) {
        List<Map<String, String>> collection = isbns.stream().map(isbn -> Map.of("isbn", isbn)).toList();
        return with()
                .spec(baseRequest)
                .header("Authorization", "Bearer " + token)
                .body(Map.of("userId", userId, "collectionOfIsbns", collection))
                .when()
                .post("/BookStore/v1/Books");
    }
}
