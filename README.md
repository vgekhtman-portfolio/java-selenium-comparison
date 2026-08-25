# Java Selenium Comparison

A portfolio project demonstrating three approaches to Java UI test automation against the public Restful Booker application.

## Purpose

The project implements the same UI test scenarios using:

1. Plain Selenium
2. Selenium with a custom wrapper
3. Selenide

The objective is to demonstrate not only framework knowledge, but also the ability to choose and design appropriate levels of abstraction.

---

# Architecture

```text
                     Test Scenarios
                           +
                        Test Data
                           |
           +---------------+---------------+
           |               |               |
           v               v               v
    Plain Selenium   Custom Selenium    Selenide
           |               |               |
      Page Objects     Page Objects     Page Objects
           |           Components       Components
           |               |               |
           |          UI abstraction       |
           |               |               |
           +---------------+---------------+
                           |
                          SUT
```

The same scenarios are implemented independently in each module.

The common layer contains neutral test data and scenario information rather than a shared UI automation framework.

# System Under Test

Primary UI:

https://automationintesting.online/

Supporting API:

The SUT's own REST API, at the same origin as the UI
(https://automationintesting.online/api/...).

The API is used only where it provides useful deterministic test-data setup, cleanup, or supporting verification.

The primary automation subject is the UI.

# Implementations
## 1. Plain Selenium

The baseline implementation uses:

* Selenium WebDriver
* By locators
* Page Objects
* Selenium explicit waits
* JUnit

The implementation intentionally uses minimal abstraction.

PageFactory and @FindBy are not used as the primary element model.

Architecture:
```
Test
|
Page Object
|
By
|
WebDriver
```
## 2. Selenium with Custom Framework

The second implementation introduces a project-owned abstraction around Selenium.

It demonstrates:

* Page Objects
* Component Objects
* reusable UI actions
* centralized synchronization
* driver management
* configuration
* diagnostics

Architecture:
```
Test
|
Page Object
|
Component
|
UI abstraction
|
WebDriver
```
The abstraction is intended to demonstrate situations where a project-specific framework layer can improve consistency and maintainability.

## 3. Selenide

The third implementation uses Selenide's native abstractions.

It demonstrates:

* Selenide elements
* conditions
* built-in synchronization
* Page Objects
* Components where useful
* Selenide configuration

Architecture:
```
Test
|
Page Object
|
Component
|
Selenide
|
WebDriver
```
The Selenide implementation does not reproduce the custom Selenium framework.

# Test Scenarios

The suite intentionally focuses on a small set of meaningful scenarios rather than exhaustive application coverage.

Current target scenarios include:

* Application smoke/readiness
* Create booking with minimal valid data
* Create booking with complete data
* Booking validation
* Booking lifecycle
* Booking retrieval where supported by the UI
* State persistence
* Synchronization/wait strategies

The same scenarios are implemented in all three approaches.

# Synchronization

Synchronization is treated as a cross-cutting framework capability.

The project demonstrates:

## Selenium

Explicit Selenium waits and conditions.

## Custom Selenium

Centralized synchronization through the custom UI abstraction.

## Selenide

Selenide conditions and built-in synchronization.

Fixed sleeps are avoided.

The project does not artificially introduce asynchronous behavior that does not exist in the SUT.

# Test Data

Test data is designed to support:

* repeatable execution
* test independence
* parallel execution
* controlled setup and cleanup

Where appropriate, the Restful Booker API may be used to create or remove known test data.

# Reporting

Allure is used for test reporting.

Reports should provide:

* readable test names
* useful test steps
* assertions
* failure information
* screenshots where appropriate

Each module writes its results into one shared directory (`target/allure-results`)
at the project root, so a single run of any or all modules produces one combined
report. Generate and view it locally:

```bash
mvn clean test
mvn -N io.qameta.allure:allure-maven:serve -Dreport.version=2.34.1
```

`serve` opens the report in a browser and keeps a local server running until you
stop it with `Ctrl+C` in that terminal.

# CI

GitHub Actions is used for automated test execution.

The CI setup supports the individual implementations and the complete suite.

# Docker

Docker provides a reproducible test execution environment for local use only
(CI runs natively - see below). The public SUT is not containerized by this
project.

Run the full suite:

```bash
docker-compose up
```

Run a single module:

```bash
MODULE=selenium-basic docker-compose up
```

Run a subset of modules:

```bash
MODULE=selenium-basic,selenide docker-compose up
```

`MODULE` accepts any Maven `-pl` value - a single module name or a
comma-separated list; omit it (or use `all`) for the full suite. Allure
results land in `target/allure-results` on the host either way, same as a
local run - see [Reporting](#reporting) above to generate the combined report.

# Project Structure
```
java-selenium-comparison/
│
├── common/
│
├── selenium-basic/
│
├── selenium-framework/
│
├── selenide/
│
├── docs/
│   ├── architecture.md
│   ├── test-scenarios.md
│   ├── framework-comparison.md
│   ├── implementation-plan.md
│   └── project-decisions.md
│
├── .github/
│   └── workflows/
│
├── pom.xml
├── docker-compose.yml
└── README.md
```
# Framework Comparison

The project is intended to illustrate trade-offs rather than declare a universal winner.

| Area                 | Plain Selenium    | Custom Selenium       | Selenide            |
|----------------------|-------------------|-----------------------|---------------------|
| Abstraction          | Minimal           | Project-owned         | Framework-provided  |
| Locator model        | By                | By behind abstraction | Selenide elements   |
| Waiting              | Explicit Selenium | Centralized custom    | Built-in/conditions |
| Components           | Limited           | Yes                   | Yes                 |
| Control              | High              | High                  | Higher-level        |
| Boilerplate          | Higher            | Medium                | Lower               |
| Framework complexity | Low               | Medium                | Low/medium          |

The comparison should focus on:

* readability
* maintainability
* extensibility
* boilerplate
* synchronization
* control
* abstraction cost

Performance measurements, if included, should be treated as observations from this project rather than general framework benchmarks.

# Running the Project

The exact Maven commands should be documented once implementation is complete.

The intended execution model is:

* Run basic Selenium tests
* Run custom Selenium tests
* Run Selenide tests
* Run all tests

Docker and GitHub Actions should provide equivalent supported execution paths.

# Project Goals

The project is considered successful if it demonstrates:

* strong Java/Selenium fundamentals
* practical Page Object design
* meaningful Component Object usage
* framework abstraction design
* synchronization expertise
* Selenide proficiency
* test-data management
* test independence
* CI/CD integration
* maintainable automation architecture

The goal is not maximum test count or maximum framework complexity.