# language: en
@ui
Feature: Web Tables CRUD

  Scenario: The user creates a record
    Given the user accesses Web Tables from the Elements menu
    When the user creates a unique record in the table
    Then the created record is displayed with the expected data

  Scenario: The user edits a record
    Given the user has created a record in the table with the following data
      | firstName | lastName | email                    | age | salary | department |
      | Marina    | Souza    | marina.souza@example.com | 34  | 8500   | Compliance |
    When the user updates the record with the following data
      | department       |
      | Quality Assurance |
    Then the updated data is displayed in the table

  Scenario: The user deletes a record
    Given the user has created a unique record in the table
    When the user deletes the created record
    Then the deleted record is no longer displayed in the table
