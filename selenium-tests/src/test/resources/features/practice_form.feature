# language: en
@ui
Feature: Practice Form

  Scenario: Student submits the Practice Form and checks the confirmation
    Given the student opens the Practice Form page
    When the student fills in all fields with valid data and submits the form
    Then the confirmation modal displays all submitted values
    And the student closes the confirmation modal
    Then the confirmation modal is no longer displayed
