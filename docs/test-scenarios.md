# Test Scenarios

## Purpose

These scenarios define the functional behavior that must be implemented in all three UI automation approaches.

The same scenario IDs and intended behavior must be preserved across:

- Plain Selenium
- Selenium with custom framework
- Selenide

The implementation details may differ.

---

# TC-UI-001 — Application Smoke & Readiness

## Intent

Verify that the application is reachable and the primary UI is ready for interaction.

## Preconditions

- Restful Booker UI is available.

## Steps

1. Open the application.
2. Verify the expected URL.
3. Verify the page title.
4. Verify the primary booking interface is present.
5. Verify critical controls are available for interaction.

## Expected Results

- Application loads successfully.
- URL is correct.
- Page title is correct.
- Primary booking UI is displayed.
- Required controls are actionable.

## Automation Focus

- browser initialization
- navigation
- locators
- basic synchronization
- page object structure

---

# TC-BOOK-001 — Create Booking: Minimal Valid Data

## Intent

Verify the primary booking flow with valid mandatory information.

## Preconditions

- Application is available.
- A suitable room/booking option is available.

## Test Data

Use a reusable valid booking-data object.

## Steps

1. Open the booking interface.
2. Enter all mandatory guest information.
3. Provide required booking information.
4. Submit the booking.
5. Wait for the resulting UI state.
6. Verify that the booking was successfully created.
7. Verify the relevant displayed booking information.

## Expected Results

- Booking submission succeeds.
- Confirmation/result state is displayed.
- Entered data is represented correctly.

## Automation Focus

- Page Objects
- form interaction
- synchronization
- assertions
- test data handling

---

# TC-BOOK-002 — Create Booking: Complete Data

## Intent

Verify all relevant booking fields supported by the SUT.

## Steps

1. Open the booking interface.
2. Populate all applicable fields with valid data.
3. Submit the booking.
4. Wait for the resulting state.
5. Verify successful creation.
6. Verify all relevant displayed/stored values.

## Expected Results

All supported input values are accepted and correctly represented after submission.

## Automation Focus

- multiple field types
- reusable form/component abstractions
- locator strategy
- data-driven interaction

---

# TC-BOOK-003 — Booking Form Validation

## Intent

Verify validation behavior actually supported by the SUT.

## Scope

Only implement validation rules that are demonstrably present in the application.

Potential examples:

- missing mandatory data
- invalid values
- invalid dates where supported
- invalid formats where supported

Do not invent validation rules merely to increase coverage.

## Steps

1. Open the relevant booking form.
2. Provide invalid or incomplete data according to the selected validation case.
3. Attempt to submit.
4. Observe validation behavior.
5. Verify the expected validation response.

## Expected Results

The application rejects or handles invalid input according to its actual implemented behavior.

## Automation Focus

- negative testing
- parameterization
- validation assertions
- reusable test data

---

# TC-BOOK-004 — Booking Lifecycle

## Intent

Verify booking state transitions through the complete lifecycle.

## Steps

1. Create a booking.
2. Identify the created booking.
3. Verify its initial state.
4. Modify one or more fields.
5. Verify the updated information.
6. Delete the booking.
7. Verify that the booking is no longer available.

## Expected Results

- Booking is created.
- Initial values are correct.
- Update succeeds.
- Updated values are correct.
- Delete succeeds.
- Deleted booking cannot be retrieved through the relevant UI flow.

## Test Data

The test must use data isolated from other tests.

API setup/cleanup may be used when necessary for deterministic execution.

## Automation Focus

- state transitions
- reusable page/component methods
- test-data isolation
- CRUD verification

---

# TC-BOOK-005 — Booking Retrieval

## Intent

Verify retrieval of existing booking data through UI functionality actually exposed by the SUT.

## Steps

1. Prepare or create a known booking.
2. Use the relevant UI functionality to retrieve/locate the booking.
3. Verify the expected booking is displayed.
4. Where the UI supports it, attempt retrieval of a non-existing booking.
5. Verify the appropriate empty/not-found behavior.

## Expected Results

- Existing booking is correctly retrieved.
- Non-existing data produces the application's expected result.

## Note

If the current SUT UI does not expose sufficient search/retrieval functionality, reduce or remove this scenario rather than introducing artificial UI behavior.

---

# TC-BOOK-006 — State Persistence

## Intent

Verify that booking state remains consistent after a page refresh or navigation.

## Steps

1. Create or update a booking.
2. Verify the resulting state.
3. Refresh or revisit the relevant UI.
4. Retrieve the same booking.
5. Verify that the expected state remains unchanged.

## Expected Results

Booking information remains consistent after navigation or page reload.

## Automation Focus

- state verification
- navigation
- persistence
- test independence

---

# TC-SYNC-001 — Synchronization: Booking Submission

## Intent

Demonstrate reliable synchronization without arbitrary fixed delays.

## Steps

1. Open the booking page.
2. Wait until the booking form is ready for interaction.
3. Enter valid booking data.
4. Submit the booking.
5. Wait until the expected post-submission state is reached.
6. Verify the booking result.
7. Confirm that no fixed sleep is required.

## Implementation Comparison

### Plain Selenium

Use:

- `WebDriverWait`
- Selenium expected conditions

### Custom Selenium

Use:

- centralized framework synchronization
- custom UI conditions where appropriate

### Selenide

Use:

- Selenide conditions
- Selenide's built-in synchronization

## Important

Do not artificially introduce asynchronous behavior.

Use synchronization only where required by actual SUT behavior.

---

# TC-SYNC-002 — Synchronization: Conditional Interaction

## Intent

Demonstrate synchronization around a UI element whose state changes during the actual SUT flow.

## Steps

1. Identify a suitable conditional interaction in the actual SUT.
2. Open the relevant page.
3. Locate the target control.
4. Wait until it reaches the required state.
5. Perform the interaction.
6. Verify the resulting state.

## Important

Only implement this scenario if a genuine suitable interaction exists in the SUT.

Do not manufacture asynchronous behavior for the purpose of the portfolio.

If no suitable interaction exists, omit this scenario.

---

# Scenario Matrix

| Scenario | Basic Selenium | Custom Selenium | Selenide |
|---|---:|---:|---:|
| UI smoke | Yes | Yes | Yes |
| Minimal booking | Yes | Yes | Yes |
| Complete booking | Yes | Yes | Yes |
| Validation | Yes | Yes | Yes |
| Lifecycle | Yes | Yes | Yes |
| Retrieval | Yes, if supported | Yes, if supported | Yes, if supported |
| Persistence | Yes | Yes | Yes |
| Synchronization | Yes | Yes | Yes |

---

# Scenario Design Principles

The three implementations must preserve:

- scenario intent
- test data semantics
- expected outcomes
- test IDs

They may differ in:

- Page Object implementation
- Component Objects
- locator representation
- waits
- UI abstractions
- assertions where framework-specific behavior makes this appropriate
- configuration
- driver management