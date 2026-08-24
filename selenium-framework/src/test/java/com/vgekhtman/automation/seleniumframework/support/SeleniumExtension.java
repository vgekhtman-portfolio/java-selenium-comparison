package com.vgekhtman.automation.seleniumframework.support;

import com.vgekhtman.automation.seleniumframework.config.FrameworkConfig;
import com.vgekhtman.automation.seleniumframework.driver.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

/** Labels each test with its Allure epic and quits the driver after each test, saving a failure screenshot first. */
public class SeleniumExtension implements BeforeEachCallback, AfterEachCallback, TestExecutionExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SeleniumExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        Allure.label("epic", "Custom Selenium Framework");
    }

    @Override
    public void afterEach(ExtensionContext context) {
        DriverManager.quitDriver();
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (FrameworkConfig.screenshotOnFailure() && DriverManager.hasActiveDriver()) {
            captureScreenshot(DriverManager.getDriver(), context.getDisplayName());
        }
        throw throwable;
    }

    private void captureScreenshot(WebDriver driver, String testName) {
        try {
            Path targetDir = Path.of("target", "screenshots");
            Files.createDirectories(targetDir);
            Path file = targetDir.resolve(sanitize(testName) + "-" + Instant.now().toEpochMilli() + ".png");
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.write(file, png);
            log.warn("Test failed - screenshot saved to {}", file.toAbsolutePath());
            Allure.addAttachment("Failure screenshot", "image/png", new ByteArrayInputStream(png), ".png");
        } catch (IOException | RuntimeException e) {
            log.warn("Could not capture failure screenshot for {}", testName, e);
        }
    }

    private String sanitize(String testName) {
        return testName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
}
