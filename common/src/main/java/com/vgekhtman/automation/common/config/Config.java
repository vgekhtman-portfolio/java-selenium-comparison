package com.vgekhtman.automation.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class Config {

    private static final Properties PROPERTIES = load();

    private Config() {
    }

    public static String baseUrl() {
        return value("baseUrl");
    }

    public static String adminUsername() {
        return value("adminUsername");
    }

    public static String adminPassword() {
        return value("adminPassword");
    }

    private static String value(String key) {
        return System.getProperty(key, PROPERTIES.getProperty(key));
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream in = Config.class.getResourceAsStream("/config.properties")) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to load config.properties", e);
        }
        return properties;
    }
}
