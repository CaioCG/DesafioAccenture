package br.com.accenture.qa.steps;

import br.com.accenture.qa.context.ScenarioState;
import br.com.accenture.qa.testdata.TestDataFactory;
import br.com.accenture.qa.testdata.model.TableRecord;
import br.com.accenture.qa.pages.Pages;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebTablesSteps {
    private final ScenarioState state;
    private final Pages pages;

    public WebTablesSteps(ScenarioState state, Pages pages) {
        this.state = state;
        this.pages = pages;
    }

    @Given("the user accesses Web Tables from the Elements menu")
    public void openWebTables() {
        pages.home().navigate().clickElements();
        pages.elements().clickWebTables();
        pages.webTables().showTwentyRows();
    }

    @Given("the user has created a unique record in the table")
    public void userHasAUniqueRecordInTable() {
        pages.home().navigate().clickElements();
        pages.elements().clickWebTables();
        pages.webTables().showTwentyRows();
        createUniqueRecordInTable();
    }

    @Given("the user has created a record in the table with the following data")
    public void userHasARecordWithData(DataTable dataTable) {
        pages.home().navigate().clickElements();
        pages.elements().clickWebTables();
        pages.webTables().showTwentyRows();
        createUniqueRecordInTable(singleRecordValues(dataTable));
    }

    @When("the user creates a unique record in the table")
    public void createUniqueRecord() {
        createUniqueRecordInTable();
    }

    private void createUniqueRecordInTable() {
        createUniqueRecordInTable(Map.of());
    }

    private void createUniqueRecordInTable(Map<String, String> providedValues) {
        TableRecord record = TestDataFactory.tableRecord(providedValues);
        state.setTableRecord(record);
        state.setCreatedRecords(List.of(record));
        pages.webTables().create(record);
    }

    @Then("the created record is displayed with the expected data")
    public void createdRecordHasExpectedValues() {
        assertRecordMatches(state.getTableRecord());
    }

    @When("the user updates the record with the following data")
    public void updateRecord(DataTable dataTable) {
        Map<String, String> changes = singleRecordValues(dataTable);
        TableRecord currentRecord = state.getTableRecord();
        TableRecord updatedRecord = currentRecord.withChanges(changes);
        state.setCreatedRecords(List.of(currentRecord, updatedRecord));
        pages.webTables()
                .updateRecord(currentRecord.email(), changes);

        state.setTableRecord(updatedRecord);
        state.setCreatedRecords(List.of(updatedRecord));
    }

    @Then("the updated data is displayed in the table")
    public void updatedRecordHasExpectedValues() {
        assertRecordMatches(state.getTableRecord());
    }

    @When("the user deletes the created record")
    public void deleteCreatedRecord() {
        pages.webTables().delete(state.getTableRecord().email());
        state.setCreatedRecords(List.of());
    }

    @Then("the deleted record is no longer displayed in the table")
    public void recordIsGone() {
        assertFalse(pages.webTables().getVisibleEmails().contains(state.getTableRecord().email()),
                "The deleted record is still displayed in the table.");
    }

    @Given("the user opens Web Tables from the Elements menu and records the existing entries")
    public void openWebTablesAndRememberBaseline() {
        openWebTables();
        state.setBaselineEmails(pages.webTables().getVisibleEmails());
    }

    @When("the user creates {int} unique records")
    public void createDynamicRecords(int count) {
        List<TableRecord> records = TestDataFactory.tableRecords(count);
        state.setCreatedRecords(records);
        for (TableRecord record : records) {
            pages.webTables().create(record);
        }
    }

    @Then("the user sees all {int} created records in the table")
    public void recordsAreVisible(int count) {
        assertEquals(count, state.getCreatedRecords().size());
        Set<String> emails = pages.webTables().getVisibleEmails();
        for (TableRecord record : state.getCreatedRecords()) {
            assertTrue(emails.contains(record.email()), "Record not found: " + record.email());
        }
    }

    @When("the user deletes all records created in this scenario")
    public void deleteAllScenarioRecords() {
        for (TableRecord record : state.getCreatedRecords()) {
            pages.webTables().deleteIfPresent(record.email());
        }
    }

    @Then("the user sees all {int} created records removed while the initial records remain")
    public void recordsAreRemovedAndBaselineRemains(int count) {
        assertEquals(count, state.getCreatedRecords().size());
        Set<String> remaining = pages.webTables().getVisibleEmails();
        for (TableRecord record : state.getCreatedRecords()) {
            assertFalse(remaining.contains(record.email()), "Record is still displayed: " + record.email());
        }
        assertTrue(remaining.containsAll(state.getBaselineEmails()),
                "An initial record disappeared; expected " + state.getBaselineEmails() + ", but found " + remaining);
        state.setCreatedRecords(List.of());
    }

    private Map<String, String> singleRecordValues(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        if (rows.size() != 1) {
            throw new IllegalArgumentException("Expected exactly one row of table data, but got " + rows.size());
        }
        return rows.getFirst();
    }

    private void assertRecordMatches(TableRecord expected) {
        List<String> actual = pages.webTables().rowValues(expected.email());
        assertTrue(actual.size() >= 6, "Expected the record to have all six table values.");
        assertEquals(expected.firstName(), actual.get(0), "First name does not match.");
        assertEquals(expected.lastName(), actual.get(1), "Last name does not match.");
        assertEquals(expected.age(), actual.get(2), "Age does not match.");
        assertEquals(expected.email(), actual.get(3), "Email does not match.");
        assertEquals(expected.salary(), actual.get(4), "Salary does not match.");
        assertEquals(expected.department(), actual.get(5), "Department does not match.");
    }
}
