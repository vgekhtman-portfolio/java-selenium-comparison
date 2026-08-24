package com.vgekhtman.automation.selenide.config;

public final class SelenideConfig {

    private SelenideConfig() {
    }

    public static String adminUsername() {
        return System.getProperty("adminUsername", "admin");
    }

    public static String adminPassword() {
        return System.getProperty("adminPassword", "password");
    }
}
