# language: en
@account
Feature: Account API
  The Account API manages user accounts and access.
  These scenarios cover account creation, access token generation,
  authorization checks, and retrieval of account details with reserved books.

  Scenario: User creates an account
    Given the user has unique account credentials
    When the user creates an account with those credentials
    Then the account is created and its identifier is returned

  Scenario: User generates an access token
    Given a user account exists with unique credentials
    When the user requests an access token
    Then an access token is returned

  Scenario: User checks account authorization
    Given a user account exists with an access token
    When the user checks authorization
    Then the API confirms the user is authorized

  Scenario: User retrieves account details with reserved books
    Given a user account has two reserved books
    When the user requests account details
    Then the account details include the user and both reserved books
