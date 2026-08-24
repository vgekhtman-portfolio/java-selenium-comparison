package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.seleniumbasic.pages.HomePage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-UI-001: Application Smoke & Readiness
@Feature("Smoke")
class ApplicationSmokeTest extends BaseUiTest {

    @Test
    @DisplayName("TC-UI-001: home page loads with the primary booking interface ready")
    void applicationIsReachableAndReady() {
        HomePage homePage = Allure.step("Open the application home page", () -> new HomePage(driver).open());

        Allure.step("Verify the application is ready for interaction", () -> {
            assertEquals("https://automationintesting.online/", homePage.currentUrl());
            assertEquals("Restful-booker-platform demo", homePage.pageTitle());
            assertTrue(homePage.isRoomsSectionDisplayed(), "Rooms section should be displayed");
            assertTrue(homePage.areBookNowControlsActionable(), "Every 'Book now' control should be actionable");
        });
    }
}
