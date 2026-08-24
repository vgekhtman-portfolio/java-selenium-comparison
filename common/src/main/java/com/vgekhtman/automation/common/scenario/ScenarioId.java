package com.vgekhtman.automation.common.scenario;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Scenario IDs shared by all three UI implementations, matching
 * docs/test-scenarios.md. Keeping these here catches ID/title drift
 * between the three modules at compile time.
 */
@Getter
@RequiredArgsConstructor
public enum ScenarioId {

    TC_UI_001("TC-UI-001", "Application Smoke & Readiness"),
    TC_BOOK_001("TC-BOOK-001", "Create Booking: Minimal Valid Data"),
    TC_BOOK_002("TC-BOOK-002", "Create Booking: Complete Data"),
    TC_BOOK_003("TC-BOOK-003", "Booking Form Validation"),
    TC_BOOK_004("TC-BOOK-004", "Booking Lifecycle"),
    TC_BOOK_005("TC-BOOK-005", "Booking Retrieval"),
    TC_BOOK_006("TC-BOOK-006", "State Persistence"),
    TC_SYNC_001("TC-SYNC-001", "Synchronization: Booking Submission"),
    TC_SYNC_002("TC-SYNC-002", "Synchronization: Conditional Interaction");

    private final String id;
    private final String title;

    @Override
    public String toString() {
        return id + " - " + title;
    }
}
