# Selenium TestNG Automation Framework

![CI](https://github.com/HSC-git-space/selenium-testng-framework/actions/workflows/ci.yml/badge.svg)

## Overview
A robust test automation framework built from scratch using Java, Selenium, and TestNG.
Designed with scalability and maintainability in mind, following industry standard
design patterns used in professional SDET roles.

## Tech Stack
- Java 11
- Selenium 4
- TestNG 7
- Maven
- Apache POI (Excel DDT)
- Extent Reports
- WebDriverManager
- GitHub Actions (CI/CD)

## Framework Features
- Page Object Model with BasePage pattern
- Data Driven Testing via Apache POI
- POJO based typed test data models
- Parallel execution via TestNG XML
- Headless Chrome support for CI
- Extent Reports with screenshot on failure
- Automated CI pipeline on every push

## Project Structure

    src/
    ├── main/java/
    │   ├── base/          → BasePage with reusable actions and waits
    │   ├── pages/         → Page classes (LoginPage)
    │   └── utils/         → ExcelUtils, ExtentReportManager
    └── test/java/
        ├── tests/         → Test classes (LoginTest)
        ├── dataproviders/ → TestNG DataProviders
        └── models/        → POJO data models (LoginData)

## Test Site
[Practice Test Automation](https://practicetestautomation.com/practice-test-login/)

## How To Run

Run all tests:

    mvn test

Run smoke suite only:

    mvn test -Dsurefire.suiteXmlFiles=testng.xml

## Test Data
Test data is managed via Excel using Apache POI.
Each row represents one test scenario with username, password and expected result.

## Reporting
Extent Reports generates an HTML report after every run.
Located at: reports/TestReport.html

## CI/CD
GitHub Actions automatically runs the full test suite on every push to master.

## Self-Healing Locator Strategy

This framework integrates [Healenium](https://healenium.io/) — an ML-based self-healing library for Selenium. When a locator fails, Healenium uses a tree-edit-distance algorithm to compare the current DOM against a stored baseline snapshot and recover the closest matching element automatically, without failing the test.

### Why This Matters
In large regression suites, UI changes can break dozens of locators overnight. Healenium reduces locator maintenance overhead during active development sprints by recovering broken locators automatically and logging what was healed for review.

### How It Works
The `SelfHealingDriver` wraps the standard `ChromeDriver` using the decorator pattern — same `WebDriver` interface, same page interactions, zero changes to existing test logic:

```java
WebDriver delegate = new ChromeDriver(options);
driver = SelfHealingDriver.create(delegate);
```

Healenium runs as a Docker service (`docker-compose.yml` included in repo root). The backend stores DOM snapshots in PostgreSQL and serves healing requests on port `7878`.

### Running Healenium Locally

Start the backend:

    docker compose up -d

Then run tests normally. Healing reports are available at:

    http://localhost:7878/healenium/report

### Known Limitation
Container stability on  Windows with WSL2 caused intermittent database authentication failures during local testing. This is a known Docker Desktop/WSL2 volume persistence issue, not a framework defect. In a Linux CI environment this does not occur.

## Retry Mechanism — Flaky Test Handling

### Why It Exists
In real test suites, not every failure is a genuine bug. Network hiccups, slow page loads, or timing issues can cause tests to fail intermittently. These are called flaky tests. Rather than marking them as failed immediately, the framework automatically retries them before reporting a final result.

### How It Works
Two classes work together to make this happen:

**RetryAnalyzer** (`src/test/java/com/sdet/listeners/RetryAnalyzer.java`)
Implements TestNG's `IRetryAnalyzer` interface. Contains a `retry()` method that returns `true` to retry a failed test and `false` to stop. Configured to retry a maximum of 2 times — meaning each test gets 3 total attempts (1 original + 2 retries) before being marked as failed.

**AnnotationTransformer** (`src/test/java/com/sdet/listeners/AnnotationTransformer.java`)
Implements TestNG's `IAnnotationTransformer` interface. Automatically applies `RetryAnalyzer` to every `@Test` method at runtime — no need to modify individual test annotations. Registered as a listener in `testng.xml`.

### Design Decision
The `IAnnotationTransformer` approach was chosen over manually adding `retryAnalyzer = RetryAnalyzer.class` to each `@Test` annotation. This keeps test classes clean and ensures retry logic is applied consistently across the entire suite without touching individual tests.

### Key Lesson
`@BeforeMethod` must have `alwaysRun = true` when retry is enabled. Without it, TestNG skips the setup method during retry attempts, leaving the WebDriver null and causing the retry to fail for the wrong reason.