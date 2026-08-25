package com.vgekhtman.automation.seleniumframework.driver;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Holds the current thread's WebDriver, created lazily on first access. */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            driver = DriverFactory.createDriver();
            DRIVER.set(driver);
        }
        return driver;
    }

    public static boolean hasActiveDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        } else {
            log.debug("quitDriver() called with no active driver on this thread - nothing to do");
        }
    }
}
