package com.vgekhtman.automation.selenide.support;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Selenide reuses one browser session across tests by default (for CI
 * speed) unless told otherwise - closing it after every test keeps tests
 * independent, since this SUT's admin session would otherwise leak from
 * one test into the next.
 */
public class SelenideExtension implements BeforeEachCallback, AfterEachCallback {

    static {
        // Native Selenide+Allure integration: reports each Selenide command as
        // a step and attaches the screenshot Selenide already takes on failure -
        // no custom screenshot/step code needed, unlike the other two modules.
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        Allure.label("epic", "Selenide");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Selenide.closeWebDriver();
    }
}
