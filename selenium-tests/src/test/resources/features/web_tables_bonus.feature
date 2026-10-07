# language: en
@ui
Feature: Web Tables bulk operations

  Scenario: The user creates twelve unique records and verifies they are visible
    Given the user opens Web Tables from the Elements menu and records the existing entries
    When the user creates 12 unique records
    Then the user sees all 12 created records in the table

  Scenario: The user deletes twelve unique records and verifies the initial entries remain
    Given the user opens Web Tables from the Elements menu and records the existing entries
    And the user creates 12 unique records
    When the user deletes all records created in this scenario
    Then the user sees all 12 created records removed while the initial records remain
