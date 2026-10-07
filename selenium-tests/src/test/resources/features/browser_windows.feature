# language: en
@ui
Feature: Browser windows

  Scenario: The user opens, verifies, and closes a new window
    Given the user opens the Browser Windows page from the Alerts, Frame & Windows menu
    When the user clicks the New Window button
    Then the sample page displays the message "This is a sample page"
    When the user closes the sample window
    Then the user returns to the original window
