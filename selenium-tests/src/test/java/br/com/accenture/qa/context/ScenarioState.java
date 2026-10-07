package br.com.accenture.qa.context;

import br.com.accenture.qa.testdata.model.TableRecord;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScenarioState {
    private WebDriver driver;
    private Map<String, String> formValues = Map.of();
    private TableRecord tableRecord;
    private List<TableRecord> createdRecords = List.of();
    private Set<String> baselineEmails = Set.of();
    private String parentWindow;
    private String childWindow;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public Map<String, String> getFormValues() {
        return formValues;
    }

    public void setFormValues(Map<String, String> formValues) {
        this.formValues = Map.copyOf(formValues);
    }

    public TableRecord getTableRecord() {
        return tableRecord;
    }

    public void setTableRecord(TableRecord tableRecord) {
        this.tableRecord = tableRecord;
    }

    public List<TableRecord> getCreatedRecords() {
        return createdRecords;
    }

    public void setCreatedRecords(List<TableRecord> createdRecords) {
        this.createdRecords = List.copyOf(createdRecords);
    }

    public Set<String> getBaselineEmails() {
        return baselineEmails;
    }

    public void setBaselineEmails(Set<String> baselineEmails) {
        this.baselineEmails = Set.copyOf(baselineEmails);
    }

    public String getParentWindow() {
        return parentWindow;
    }

    public void setParentWindow(String parentWindow) {
        this.parentWindow = parentWindow;
    }

    public String getChildWindow() {
        return childWindow;
    }

    public void setChildWindow(String childWindow) {
        this.childWindow = childWindow;
    }
}
