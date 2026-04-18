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