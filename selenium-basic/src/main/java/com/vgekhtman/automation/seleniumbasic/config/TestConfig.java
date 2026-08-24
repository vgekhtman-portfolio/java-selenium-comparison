package com.vgekhtman.automation.seleniumbasic.config;

import java.time.Duration;

public final class TestConfig {

    private TestConfig() {
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }

    public static Duration explicitWaitTimeout() {
        return Duration.ofSeconds(Long.parseLong(System.getProperty("timeoutSeconds", "10")));
    }
}
