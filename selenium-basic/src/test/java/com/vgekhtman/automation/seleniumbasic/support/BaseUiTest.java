package com.vgekhtman.automation.seleniumbasic.support;

import com.vgekhtman.automation.seleniumbasic.driver.DriverFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

public abstract class BaseUiTest {

    protected WebDriver driver;

    @BeforeEach
    void setUpDriver() {
        driver = DriverFactory.createDriver();
    }

    @AfterEach
    void tearDownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
