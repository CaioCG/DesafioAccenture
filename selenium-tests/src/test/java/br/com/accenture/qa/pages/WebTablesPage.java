package br.com.accenture.qa.pages;

import br.com.accenture.qa.testdata.model.TableRecord;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WebTablesPage extends BasePage {
    private static final By PAGE_SIZE = By.cssSelector("select.form-control");
    private static final By TABLE_ROWS = By.cssSelector("table tbody tr");
    private static final By REGISTRATION_FORM_MODAL = By.id("registration-form-modal");
    private static final By ADD_RECORD_BUTTON = By.id("addNewRecordButton");
    private static final By SUBMIT_BUTTON = By.id("submit");
    private static final By EDIT_BUTTON = By.cssSelector("[id^='edit-record-']");
    private static final By DELETE_BUTTON = By.cssSelector("[id^='delete-record-']");
    private static final Map<String, By> FORM_FIELDS = Map.of(
            "firstName", By.id("firstName"),
            "lastName", By.id("lastName"),
            "age", By.id("age"),
            "email", By.id("userEmail"),
            "salary", By.id("salary"),
            "department", By.id("department"));
    private static final Map<String, Integer> FIELD_COLUMN_INDEX = Map.of(
            "firstName", 0,
            "lastName", 1,
            "age", 2,
            "email", 3,
            "salary", 4,
            "department", 5);

    public WebTablesPage(WebDriver driver) {
        super(driver);
    }

    public WebTablesPage showTwentyRows() {
        WebElement pageSize = waitUntilVisible(PAGE_SIZE);
        new Select(pageSize).selectByValue("20");
        waitUntil(d -> new Select(d.findElement(PAGE_SIZE)).getFirstSelectedOption().getAttribute("value").equals("20"));
        return this;
    }

    public Set<String> getVisibleEmails() {
        Set<String> emails = new LinkedHashSet<>();
        for (WebElement row : driver.findElements(TABLE_ROWS)) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 3 && !cells.get(3).getText().isBlank()) {
                emails.add(cells.get(3).getText().trim());
            }
        }
        return emails;
    }

    public WebTablesPage create(TableRecord record) {
        click(ADD_RECORD_BUTTON);
        waitUntilVisible(REGISTRATION_FORM_MODAL);
        fillForm(record);
        click(SUBMIT_BUTTON);
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(REGISTRATION_FORM_MODAL));
        waitUntil(d -> !rowsForEmail(record.email()).isEmpty());
        return this;
    }

    public List<String> rowValues(String email) {
        List<WebElement> rows = rowsForEmail(email);
        if (rows.isEmpty()) {
            return List.of();
        }
        return rows.getFirst().findElements(By.tagName("td"))
                .stream().map(element -> element.getText().trim()).toList();
    }

    public WebTablesPage updateRecord(String email, Map<String, String> changes) {
        if (changes.isEmpty()) {
            throw new IllegalArgumentException("At least one table field must be provided for an update.");
        }
        for (String field : changes.keySet()) {
            if (!FORM_FIELDS.containsKey(field)) {
                throw new IllegalArgumentException("Unsupported table field: " + field);
            }
        }

        WebElement row = findOne(email);
        clickWithin(row, EDIT_BUTTON);
        waitUntilVisible(REGISTRATION_FORM_MODAL);
        changes.forEach((field, value) -> type(FORM_FIELDS.get(field), value));
        click(SUBMIT_BUTTON);
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(REGISTRATION_FORM_MODAL));

        String updatedEmail = changes.getOrDefault("email", email);
        waitUntil(d -> {
            List<String> values = rowValues(updatedEmail);
            return values.size() > FIELD_COLUMN_INDEX.values().stream().mapToInt(Integer::intValue).max().orElse(0)
                    && changes.entrySet().stream().allMatch(entry ->
                    values.get(FIELD_COLUMN_INDEX.get(entry.getKey())).equals(entry.getValue()));
        });
        return this;
    }

    public WebTablesPage delete(String email) {
        WebElement row = findOne(email);
        clickWithin(row, DELETE_BUTTON);
        waitUntil(d -> rowsForEmail(email).isEmpty());
        return this;
    }

    public WebTablesPage deleteIfPresent(String email) {
        if (!driver.getCurrentUrl().contains("/webtables")) {
            return this;
        }
        showTwentyRows();
        List<WebElement> rows = rowsForEmail(email);
        if (!rows.isEmpty()) {
            clickWithin(rows.getFirst(), DELETE_BUTTON);
            waitUntil(d -> rowsForEmail(email).isEmpty());
        }
        return this;
    }

    private void fillForm(TableRecord record) {
        type(FORM_FIELDS.get("firstName"), record.firstName());
        type(FORM_FIELDS.get("lastName"), record.lastName());
        type(FORM_FIELDS.get("age"), record.age());
        type(FORM_FIELDS.get("email"), record.email());
        type(FORM_FIELDS.get("salary"), record.salary());
        type(FORM_FIELDS.get("department"), record.department());
    }

    private WebElement findOne(String email) {
        return waitUntil(d -> {
            List<WebElement> rows = rowsForEmail(email);
            return rows.isEmpty() ? null : rows.getFirst();
        });
    }

    private List<WebElement> rowsForEmail(String email) {
        List<WebElement> matching = new ArrayList<>();
        for (WebElement row : driver.findElements(TABLE_ROWS)) {
            try {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() > 3 && cells.get(3).getText().trim().equals(email)) {
                    matching.add(row);
                }
            } catch (StaleElementReferenceException ignored) {
                // React Table may replace the row while the search result is updating.
            }
        }
        return matching;
    }

    private void clickWithin(WebElement row, By control) {
        waitUntil(d -> {
            try {
                WebElement button = row.findElement(control);
                if (!button.isDisplayed()) {
                    return false;
                }
                button.click();
                return true;
            } catch (StaleElementReferenceException exception) {
                return false;
            }
        });
    }
}
