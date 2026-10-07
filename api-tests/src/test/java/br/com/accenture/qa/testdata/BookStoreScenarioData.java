package br.com.accenture.qa.testdata;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class BookStoreScenarioData {
    private BookStoreScenarioData() {
    }

    public static List<String> isbns(Response response) {
        List<Map<String, Object>> books = response.jsonPath().getList("books");
        assertNotNull(books, response.asString());
        return books.stream()
                .filter(Objects::nonNull)
                .map(book -> book.get("isbn"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .filter(isbn -> !isbn.isBlank())
                .distinct()
                .toList();
    }

    public static List<String> firstTwoAvailableIsbns(Response response) {
        List<String> availableIsbns = isbns(response);
        assertTrue(availableIsbns.size() >= 2, "The API should provide at least two distinct ISBNs.");
        return availableIsbns.subList(0, 2);
    }
}
