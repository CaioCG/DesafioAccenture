package br.com.accenture.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import java.util.ArrayList;
import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT;

public class SortablePage extends BasePage {
    private static final List<String> ASCENDING = List.of("One", "Two", "Three", "Four", "Five", "Six");
    private static final By ITEMS = By.cssSelector("#demo-tabpane-list .list-group-item");

    public SortablePage(WebDriver driver) {
        super(driver);
    }

    public SortablePage arrangeAscending() {
        waitUntil(d -> d.findElements(ITEMS).size() == ASCENDING.size());
        List<String> current = getItemTexts();
        if (current.equals(ASCENDING)) {
            List<String> scrambled = new ArrayList<>(ASCENDING);
            scrambled.removeFirst();
            scrambled.add("One");
            dragItemToIndex(0, current.size() - 1, scrambled);
        }

        for (int targetIndex = 0; targetIndex < ASCENDING.size(); targetIndex++) {
            current = getItemTexts();
            String wanted = ASCENDING.get(targetIndex);
            if (current.get(targetIndex).equals(wanted)) {
                continue;
            }
            int sourceIndex = current.indexOf(wanted);
            List<String> expected = new ArrayList<>(current);
            String movingItem = expected.remove(sourceIndex);
            expected.add(targetIndex, movingItem);
            dragItemToIndex(sourceIndex, targetIndex, expected);
        }
        return this;
    }

    public List<String> getItemTexts() {
        return driver.findElements(ITEMS).stream().map(item -> item.getText().trim()).toList();
    }

    public List<String> ascendingOrder() {
        return ASCENDING;
    }

    private void dragItemToIndex(int sourceIndex, int targetIndex, List<String> expected) {
        List<WebElement> items = driver.findElements(ITEMS);
        WebElement source = items.get(sourceIndex);
        WebElement target = items.get(targetIndex);
        int targetOffsetY = Math.max(1, target.getSize().getHeight() / 2 - 2)
                * Integer.signum(targetIndex - sourceIndex);

        var action = new Actions(driver);
        action
            .tick(action.getActivePointer()
                    .createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.fromElement(source), 100, 0))
            .tick(action.getActivePointer().createPointerDown(LEFT.asArg()))
            .tick(action.getActivePointer()
                    .createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.fromElement(target), 100, targetOffsetY))
            .pause(Duration.ofMillis(150))
            .tick(action.getActivePointer().createPointerUp(LEFT.asArg()))
            .perform();

        waitForOrder(expected, Duration.ofSeconds(3));
    }

    private void waitForOrder(List<String> expected, Duration duration) {
        try {
            waitUntil(d -> getItemTexts().equals(expected), duration);
        } catch (TimeoutException exception) {
            throw new IllegalStateException("Drag and drop did not produce the expected order. Expected: "
                    + expected + "; actual: " + getItemTexts(), exception);
        }
    }

}
