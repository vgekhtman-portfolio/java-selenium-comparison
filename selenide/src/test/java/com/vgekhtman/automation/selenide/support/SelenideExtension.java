package com.vgekhtman.automation.selenide.support;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

/**
 * Selenide reuses one browser session across tests by default (for CI
 * speed) unless told otherwise - closing it after every test keeps tests
 * independent, since this SUT's admin session would otherwise leak from
 * one test into the next.
 */
public class SelenideExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        Selenide.closeWebDriver();
    }
}
