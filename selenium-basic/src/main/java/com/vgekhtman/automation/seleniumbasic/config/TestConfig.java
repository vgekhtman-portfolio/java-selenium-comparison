package com.vgekhtman.automation.seleniumbasic.config;

import java.time.Duration;

public final class TestConfig {

    private TestConfig() {
    }

    public static String baseUrl() {
        return System.getProperty("baseUrl", "https://automationintesting.online");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "true"));
    }

    public static Duration explicitWaitTimeout() {
        return Duration.ofSeconds(Long.parseLong(System.getProperty("timeoutSeconds", "10")));
    }

    public static String adminUsername() {
        return System.getProperty("adminUsername", "admin");
    }

    public static String adminPassword() {
        return System.getProperty("adminPassword", "password");
    }
}
