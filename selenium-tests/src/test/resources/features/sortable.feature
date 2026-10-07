# language: en
@ui
Feature: Sortable items

  Scenario: The user sorts the list in ascending order
    Given the user opens the Sortable page from the Interactions menu
    When the user sorts the items using drag and drop
    Then the user sees the items in ascending order from One to Six
