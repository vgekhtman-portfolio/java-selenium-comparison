# Java Selenium Comparison

[![CI](https://github.com/vgekhtman-portfolio/java-selenium-comparison/actions/workflows/ci.yml/badge.svg)](https://github.com/vgekhtman-portfolio/java-selenium-comparison/actions/workflows/ci.yml)

A portfolio project demonstrating three approaches to Java UI test automation against the public Restful-Booker-Platform demo (automationintesting.online).

## Purpose

The project implements the same UI test scenarios using:

1. Plain Selenium
2. Selenium with a custom wrapper
3. Selenide

The objective is to demonstrate not only framework knowledge, but also the ability to choose and design appropriate levels of abstraction.

---

# Running the Tests

Requires Java 21+, Maven, and a local Chrome installation - or Docker, which needs neither (see below).

## Maven

Run all three implementations:

```bash
mvn test
```

Run one implementation:

```bash
mvn -pl selenium-basic -am test
mvn -pl selenium-framework -am test
mvn -pl selenide -am test
```

Tests run headless by default. To watch them in a visible browser:

```bash
mvn test -Dheadless=false -Dselenide.headless=false
```

## Docker

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
comma-separated list; omit it (or use `all`) for the full suite.

## Viewing the Allure Report

Each module writes its results into one shared directory (`target/allure-results`)
at the project root, so a single run of any or all modules - via Maven or Docker -
produces one combined report:

```bash
mvn -N io.qameta.allure:allure-maven:serve -Dreport.version=2.34.1
```

`serve` opens the report in a browser and keeps a local server running until you
stop it with `Ctrl+C` in that terminal.

GitHub Actions generates and publishes the same combined report automatically -
see [CI](#ci) below.

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
The abstraction demonstrates situations where a project-specific framework layer improves consistency and maintainability.

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

The suite implements:

* Application smoke/readiness
* Create booking with minimal valid data
* Create booking with complete data
* Booking validation
* Booking lifecycle
* Booking retrieval via the admin booking list
* State persistence
* Synchronization/wait strategies

The same scenarios are implemented in all three approaches.

# Synchronization

Synchronization is a cross-cutting concern, addressed differently by each implementation:

## Selenium

Explicit Selenium waits and conditions.

## Custom Selenium

Centralized synchronization through the custom UI abstraction.

## Selenide

Selenide conditions and built-in synchronization.

Fixed sleeps are avoided.

The project does not artificially introduce asynchronous behavior that does not exist in the SUT.

# Test Data

Test data supports:

* repeatable execution
* test independence
* parallel execution
* controlled setup and cleanup

Where appropriate, the SUT's own API (see [System Under Test](#system-under-test)) may be used to create or remove known test data.

# Reporting

Allure is used for test reporting.

Reports provide:

* readable test names
* useful test steps
* assertions
* failure information
* failure screenshots (Selenide and the custom framework capture these automatically; plain Selenium does not, in keeping with its minimal-abstraction design)

See [Running the Tests](#running-the-tests) above to generate and view the report.

# CI

GitHub Actions runs each of the three modules as its own job, so any one
implementation's result is visible independently; running all three together
is the normal full-suite run. A combined Allure report across all three is
generated afterward and published to GitHub Pages, carrying trend history
forward from the previously published report:

https://vgekhtman-portfolio.github.io/java-selenium-comparison/

# Docker

Docker provides a reproducible test execution environment for local use only
(CI runs natively - see [CI](#ci) above). The public SUT is not containerized
by this project. See [Running the Tests](#running-the-tests) above for the
commands.

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
│   └── test-scenarios.md
│
├── .github/
│   └── workflows/
│
├── pom.xml
├── docker-compose.yml
└── README.md
```
# Framework Comparison

The project illustrates trade-offs rather than declaring a universal winner.

| Area                 | Plain Selenium    | Custom Selenium       | Selenide            |
|----------------------|-------------------|-----------------------|---------------------|
| Abstraction          | Minimal           | Project-owned         | Framework-provided  |
| Locator model        | By                | By behind abstraction | Selenide elements   |
| Waiting              | Explicit Selenium | Centralized custom    | Built-in/conditions |
| Components           | None              | Yes                   | Yes                 |
| Control              | High              | High                  | Higher-level        |
| Boilerplate          | Higher            | Medium                | Lower               |
| Framework complexity | Low               | Medium                | Low/medium          |

The comparison focuses on:

* readability
* maintainability
* extensibility
* boilerplate
* synchronization
* control
* abstraction cost

# Design Decisions

* **No shared UI automation framework in `common`.** The common module holds
  neutral test data and scenario information only, so each implementation's
  synchronization, abstraction and driver-management choices stay genuinely
  independent instead of converging on one framework in disguise.
* **Test dates are randomized across a wide future window (1-3650 days
  out).** The SUT enforces real room+date conflicts server-side, even
  against other, unrelated users of this shared public demo - a fixed date
  would eventually collide.
* **Flaky runs are handled with Surefire retries (`rerunFailingTestsCount`),
  not custom recovery code.** An earlier crash-recovery listener was removed
  once retries alone showed a 100% recovery rate for every flake observed -
  the simpler mechanism already covers it.
* **Docker is local-only; CI runs natively.** GitHub-hosted runners already
  provide Chrome and a consistent environment, so containerizing CI would
  add overhead without solving a real problem there.
* **CI runs the three modules as independent matrix jobs.** Each
  implementation's pass/fail is visible on its own rather than folded into
  one aggregate result.

# Limitations

* Tests run against a shared public demo instance. Despite date
  randomization and retries, occasional flakiness or conflicts caused by
  other users of the same instance are possible.
* Chrome only - there is no cross-browser coverage.
* Docker is not exercised in CI (see [Design Decisions](#design-decisions)).

# Potential Improvements

* **Broader scenario coverage.** The current scenarios cover the primary
  booking flows; edge cases (concurrent modification, boundary values beyond
  field-length checks, error-page handling) are not exercised.
* **Multi-browser support.** Firefox and Edge/WebKit via Selenium's
  cross-browser API, exercised as an additional CI matrix dimension
  alongside the three implementations (see [Limitations](#limitations)).
* **Smarter flakiness handling.** `rerunFailingTestsCount` treats every
  failure as a plain retry; distinguishing genuine SUT flakiness (409
  conflicts, transient network) from real regressions would give more
  signal than a blanket retry.
* **A dedicated/isolated SUT instance.** The shared public demo is the
  biggest source of environmental risk; a self-hosted instance of the same
  app would remove cross-user collisions entirely instead of just
  mitigating them.
* **Visual regression and accessibility checks.** Neither is currently in
  scope; both are natural extensions given the UI-first nature of the
  project.