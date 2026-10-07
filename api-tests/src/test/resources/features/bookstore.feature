# language: en
@bookstore
Feature: Book Store API
  The Book Store API provides the available book catalog and lets an
  authenticated user add selected books to their collection.
  These scenarios cover retrieving the catalog and adding books for a user.

  Scenario: User lists available books
    When the user requests the available books
    Then the catalog includes at least two books with ISBNs

  Scenario: User adds two books to their collection
    Given the user has a valid access token and two available books are selected
    When the user adds the selected books to their collection
    Then the user's collection includes both selected ISBNs
