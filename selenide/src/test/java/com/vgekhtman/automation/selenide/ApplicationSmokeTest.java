package com.vgekhtman.automation.selenide;

import com.vgekhtman.automation.selenide.support.SelenideExtension;
import com.vgekhtman.automation.selenide.pages.HomePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-UI-001: Application Smoke & Readiness
@ExtendWith(SelenideExtension.class)
class ApplicationSmokeTest {

    @Test
    @DisplayName("TC-UI-001: home page loads with the primary booking interface ready")
    void applicationIsReachableAndReady() {
        HomePage homePage = new HomePage().open();

        assertEquals("https://automationintesting.online/", homePage.currentUrl());
        assertEquals("Restful-booker-platform demo", homePage.pageTitle());
        assertTrue(homePage.isRoomsSectionDisplayed(), "Rooms section should be displayed");
        assertTrue(homePage.areBookNowControlsActionable(), "Every 'Book now' control should be actionable");
    }
}
