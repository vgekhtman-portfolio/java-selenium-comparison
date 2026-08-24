package com.vgekhtman.automation.seleniumframework.driver;

import com.vgekhtman.automation.seleniumframework.config.FrameworkConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        if (FrameworkConfig.headless()) {
            options.addArguments("--headless=new");
        }
        // --window-size sets the OS window, not the viewport, and headless
        // Chrome's chrome-less viewport still ends up a bit smaller than
        // requested - go generously tall so scrollIntoView(center) always
        // has room to place a target element fully inside the viewport.
        options.addArguments("--window-size=1440,1600");

        WebDriver driver = new ChromeDriver(options);
        // Explicit waits only - mixing an implicit wait with WebDriverWait
        // makes wait times unpredictable and hard to debug.
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        log.debug("Created Chrome driver session {}", driver);
        return driver;
    }
}
