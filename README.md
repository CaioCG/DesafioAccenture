# QA Automation Challenge — DemoQA

An automation suite for the Accenture challenge, built with Java 21, Selenium WebDriver, RestAssured, Cucumber, and JUnit Platform. It covers the Book Store API and the Practice Form, Browser Windows, Web Tables, Progress Bar, and Sortable UI flows, including the bonus scenario with 12 records.

## Execution demo

[Watch the project execution recording](./execution-recording.mov) (MOV).

## Prerequisites

- JDK 21
- Google Chrome
- Internet access to download dependencies and access `https://demoqa.com`

Selenium Manager resolves ChromeDriver automatically. The Maven Wrapper downloads and uses the Maven version pinned by the project.

If SDKMAN is installed, install and activate the versions listed in `.sdkmanrc` with:

```bash
sdk env install
sdk env
```

## Run the tests

Run both modules from the project root:

```bash
./mvnw verify
```

The project has two modules: `api-tests` for the Book Store API and `selenium-tests` for browser tests. Run either module on its own:

```bash
./mvnw -f api-tests/pom.xml verify
./mvnw -f selenium-tests/pom.xml verify
```

From inside either module directory, run `../mvnw verify`. Chrome runs in headless mode by default. To watch the browser during the Selenium tests:

```bash
./mvnw -f selenium-tests/pom.xml verify -Dheadless=false
```

Override the base URLs with the Maven properties `-Ddemoqa.api.base-url=...` and `-Ddemoqa.ui.base-url=...`. From the project root, run only UI scenarios by Cucumber tag with:

```bash
./mvnw -f selenium-tests/pom.xml verify -Dcucumber.filter.tags="@ui"
```

## Reports

Cucumber HTML and JSON reports are written to `api-tests/target/cucumber-reports/` or `selenium-tests/target/cucumber-reports/`, depending on the module. UI failure reports also include a browser screenshot.

## Test data

The tests use unique identifiers for generated accounts and dynamically created UI records. The UI suite removes records it creates during scenario teardown, and the API suite attempts to delete each test account after a scenario.
