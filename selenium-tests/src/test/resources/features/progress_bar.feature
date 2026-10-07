# language: en
@ui
Feature: Progress bar

  Scenario: The user pauses, completes, and resets the progress bar
    Given the user accesses the Progress Bar page from the Widgets menu
    When the user starts the progress bar and stops at or below 25 percent
    Then the progress value is at or below 25 percent
    When the user restarts the progress bar and waits for completion
    Then the progress bar reaches 100 percent
    When the user resets the progress bar
    Then the progress bar returns to zero percent
