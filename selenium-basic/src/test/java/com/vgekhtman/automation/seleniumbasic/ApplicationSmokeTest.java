package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.seleniumbasic.pages.HomePage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-UI-001: Application Smoke & Readiness
class ApplicationSmokeTest extends BaseUiTest {

    @Test
    @DisplayName("TC-UI-001: home page loads with the primary booking interface ready")
    void applicationIsReachableAndReady() {
        HomePage homePage = new HomePage(driver).open();

        assertEquals("https://automationintesting.online/", homePage.currentUrl());
        assertEquals("Restful-booker-platform demo", homePage.pageTitle());
        assertTrue(homePage.isRoomsSectionDisplayed(), "Rooms section should be displayed");
        assertTrue(homePage.areBookNowControlsActionable(), "Every 'Book now' control should be actionable");
    }
}
