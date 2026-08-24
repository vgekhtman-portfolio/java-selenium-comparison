package com.vgekhtman.automation.seleniumframework.config;

import java.time.Duration;

public final class FrameworkConfig {

    private FrameworkConfig() {
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }

    public static Duration explicitWaitTimeout() {
        return Duration.ofSeconds(Long.parseLong(System.getProperty("timeoutSeconds", "10")));
    }

    public static boolean screenshotOnFailure() {
        return Boolean.parseBoolean(System.getProperty("screenshotOnFailure", "true"));
    }
}
