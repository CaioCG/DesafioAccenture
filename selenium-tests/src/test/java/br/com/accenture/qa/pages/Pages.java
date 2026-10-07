package br.com.accenture.qa.pages;

import br.com.accenture.qa.context.ScenarioState;
import org.openqa.selenium.WebDriver;

public class Pages {
    private final ScenarioState state;

    public Pages(ScenarioState state) {
        this.state = state;
    }

    public DemoQaHome home() {
        return new DemoQaHome(driver());
    }

    public DemoQaElementsPage elements() {
        return new DemoQaElementsPage(driver());
    }

    public DemoQaFormsPage forms() {
        return new DemoQaFormsPage(driver());
    }

    public DemoQaAlertsFrameWindowsPage alertsFrameWindows() {
        return new DemoQaAlertsFrameWindowsPage(driver());
    }

    public DemoQaWidgetsPage widgets() {
        return new DemoQaWidgetsPage(driver());
    }

    public DemoQaInteractionsPage interactions() {
        return new DemoQaInteractionsPage(driver());
    }

    public PracticeFormPage practiceForm() {
        return new PracticeFormPage(driver());
    }

    public BrowserWindowsPage browserWindows() {
        return new BrowserWindowsPage(driver());
    }

    public SamplePage sample() {
        return new SamplePage(driver());
    }

    public WebTablesPage webTables() {
        return new WebTablesPage(driver());
    }

    public ProgressBarPage progressBar() {
        return new ProgressBarPage(driver());
    }

    public SortablePage sortable() {
        return new SortablePage(driver());
    }

    private WebDriver driver() {
        WebDriver driver = state.getDriver();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized for this scenario.");
        }
        return driver;
    }
}
