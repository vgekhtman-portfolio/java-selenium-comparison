package com.vgekhtman.automation.seleniumframework.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UiInteractionException extends RuntimeException {

    public UiInteractionException(String action, String description, By locator, WebDriver driver, Throwable cause) {
        super(buildMessage(action, description, locator, driver), cause);
    }

    private static String buildMessage(String action, String description, By locator, WebDriver driver) {
        String url = safeCurrentUrl(driver);
        return "Failed to %s [%s] (%s) - current URL: %s".formatted(action, description, locator, url);
    }

    private static String safeCurrentUrl(WebDriver driver) {
        try {
            return driver.getCurrentUrl();
        } catch (RuntimeException e) {
            return "<unavailable: " + e.getMessage() + ">";
        }
    }
}
