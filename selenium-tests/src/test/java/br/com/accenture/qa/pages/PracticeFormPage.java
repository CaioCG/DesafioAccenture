package br.com.accenture.qa.pages;

import br.com.accenture.qa.testdata.model.PracticeFormData;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PracticeFormPage extends BasePage {
    private static final By FIRST_NAME_INPUT = By.id("firstName");
    private static final By LAST_NAME_INPUT = By.id("lastName");
    private static final By EMAIL_INPUT = By.id("userEmail");
    private static final By MALE_GENDER_LABEL = By.cssSelector("label[for='gender-radio-1']");
    private static final By FEMALE_GENDER_LABEL = By.cssSelector("label[for='gender-radio-2']");
    private static final By OTHER_GENDER_LABEL = By.cssSelector("label[for='gender-radio-3']");
    private static final By MOBILE_INPUT = By.id("userNumber");
    private static final By DATE_OF_BIRTH_INPUT = By.id("dateOfBirthInput");
    private static final By DATE_PICKER_YEAR_SELECT = By.cssSelector(".react-datepicker__year-select");
    private static final By DATE_PICKER_MONTH_SELECT = By.cssSelector(".react-datepicker__month-select");
    private static final By SUBJECTS_INPUT = By.id("subjectsInput");
    private static final By AUTOCOMPLETE_OPTIONS = By.cssSelector("[role='option']");
    private static final By UPLOAD_PICTURE_INPUT = By.id("uploadPicture");
    private static final By CURRENT_ADDRESS_INPUT = By.id("currentAddress");
    private static final By STATE_INPUT = By.id("react-select-3-input");
    private static final By CITY_INPUT = By.id("react-select-4-input");
    private static final By SUBMIT_BUTTON = By.id("submit");
    private static final By MODAL_CONTENT = By.cssSelector(".modal-content");
    private static final By CONFIRMATION_ROWS = By.cssSelector(".modal-body tbody tr");
    private static final By CONFIRMATION_TITLE = By.id("example-modal-sizes-title-lg");
    private static final By CLOSE_CONFIRMATION_BUTTON = By.id("closeLargeModal");
    private static final By BODY = By.tagName("body");
    private static final String HOBBY_LABEL_XPATH = "//label[normalize-space()=%s]";
    private static final String DATE_PICKER_DAY_SELECTOR_FORMAT =
            ".react-datepicker__day--%03d:not(.react-datepicker__day--outside-month)";

    public PracticeFormPage(WebDriver driver) {
        super(driver);
    }

    public void submit(PracticeFormData data, Path uploadPath) {
        type(FIRST_NAME_INPUT, data.firstName());
        type(LAST_NAME_INPUT, data.lastName());
        type(EMAIL_INPUT, data.email());

        By genderLabel = switch (data.gender()) {
            case "Male" -> MALE_GENDER_LABEL;
            case "Female" -> FEMALE_GENDER_LABEL;
            default -> OTHER_GENDER_LABEL;
        };
        click(genderLabel);
        type(MOBILE_INPUT, data.mobile());

        selectDateOfBirth(data.dateOfBirth());

        selectAutocompleteOption(SUBJECTS_INPUT, data.subject());
        for (String hobby : data.hobbies()) {
            click(By.xpath(HOBBY_LABEL_XPATH.formatted(xpathLiteral(hobby))));
        }

        waitUntilVisible(UPLOAD_PICTURE_INPUT)
                .sendKeys(uploadPath.toAbsolutePath().normalize().toString());

        type(CURRENT_ADDRESS_INPUT, data.address());
        selectAutocompleteOption(STATE_INPUT, data.state());
        selectAutocompleteOption(CITY_INPUT, data.city());
        click(SUBMIT_BUTTON);
        waitUntilVisible(MODAL_CONTENT);
    }

    public Map<String, String> confirmationDetails() {
        waitUntilVisible(MODAL_CONTENT);
        List<WebElement> rows = driver.findElements(CONFIRMATION_ROWS);
        Map<String, String> values = new LinkedHashMap<>();
        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() >= 2) {
                values.put(cells.get(0).getText().trim(), cells.get(1).getText().trim());
            }
        }
        return values;
    }

    public String confirmationTitle() {
        return text(CONFIRMATION_TITLE);
    }

    public void closeConfirmation() {
        click(CLOSE_CONFIRMATION_BUTTON);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(2))
                    .until(ExpectedConditions.invisibilityOfElementLocated(MODAL_CONTENT));
        } catch (TimeoutException ignored) {
            driver.findElement(BODY).sendKeys(Keys.ESCAPE);
        }
        waitUntil(ExpectedConditions.invisibilityOfElementLocated(MODAL_CONTENT));
    }

    public boolean isConfirmationModalOpen() {
        return driver.findElements(MODAL_CONTENT).stream().anyMatch(WebElement::isDisplayed);
    }

    private void selectDateOfBirth(LocalDate date) {
        click(DATE_OF_BIRTH_INPUT);
        new Select(waitUntilVisible(DATE_PICKER_YEAR_SELECT))
                .selectByValue(String.valueOf(date.getYear()));
        new Select(waitUntilVisible(DATE_PICKER_MONTH_SELECT))
                .selectByValue(String.valueOf(date.getMonthValue() - 1));
        click(By.cssSelector(DATE_PICKER_DAY_SELECTOR_FORMAT.formatted(date.getDayOfMonth())));
    }

    private void selectAutocompleteOption(By inputLocator, String value) {
        WebElement input = clickable(inputLocator);
        input.click();
        input.sendKeys(value);

        WebElement option = waitUntil(d -> d.findElements(AUTOCOMPLETE_OPTIONS).stream()
                .filter(WebElement::isDisplayed)
                .filter(candidate -> candidate.getText().trim().equals(value))
                .findFirst()
                .orElse(null));
        option.click();
    }

    private String xpathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "',\"'\",'") + "')";
    }
}
