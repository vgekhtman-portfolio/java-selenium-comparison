package com.vgekhtman.automation.seleniumframework.core;

import com.vgekhtman.automation.seleniumframework.config.FrameworkConfig;
import com.vgekhtman.automation.seleniumframework.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A single interaction point: a locator plus a human-readable description.
 * Centralizes waiting, scrolling into view, logging, and failure context
 * that every Page Object would otherwise repeat by hand.
 */
public class UiElement {

    private static final Logger log = LoggerFactory.getLogger(UiElement.class);

    private final WebDriver driver;
    private final By locator;
    private final String description;
    private final WebDriverWait wait;

    public UiElement(By locator, String description) {
        this.driver = DriverManager.getDriver();
        this.locator = locator;
        this.description = description;
        this.wait = new WebDriverWait(driver, FrameworkConfig.explicitWaitTimeout());
    }

    public void click() {
        log.debug("Clicking {}", description);
        try {
            WebElement element = resolveVisible();
            scrollIntoView(element);
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (TimeoutException e) {
            throw new UiInteractionException("click", description, locator, driver, e);
        }
    }

    public void type(String value) {
        log.debug("Typing into {}", description);
        try {
            WebElement element = resolveVisible();
            element.clear();
            element.sendKeys(value);
        } catch (TimeoutException e) {
            throw new UiInteractionException("type into", description, locator, driver, e);
        }
    }

    public String text() {
        try {
            return resolveVisible().getText();
        } catch (TimeoutException e) {
            throw new UiInteractionException("read text of", description, locator, driver, e);
        }
    }

    public List<String> texts() {
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator))
                    .stream()
                    .map(WebElement::getText)
                    .toList();
        } catch (TimeoutException e) {
            throw new UiInteractionException("read text of", description, locator, driver, e);
        }
    }

    public boolean exists() {
        return !driver.findElements(locator).isEmpty();
    }

    public boolean isDisplayed() {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }

    public boolean isEnabled() {
        return driver.findElements(locator).stream().anyMatch(WebElement::isEnabled);
    }

    /** For a locator matching several elements: waits until every one is displayed and enabled. */
    public boolean allDisplayedAndEnabled() {
        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(locator);
                return !elements.isEmpty() && elements.stream().allMatch(e -> e.isDisplayed() && e.isEnabled());
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    public UiElement waitUntilVisible() {
        resolveVisible();
        return this;
    }

    public UiElement waitUntilClickable() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new UiInteractionException("wait for", description, locator, driver, e);
        }
        return this;
    }

    public UiElement waitUntilGone() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new UiInteractionException("wait for removal of", description, locator, driver, e);
        }
        return this;
    }

    public UiElement waitUntilTextEquals(String expected) {
        try {
            wait.until(d -> expected.equals(text()));
        } catch (TimeoutException e) {
            throw new UiInteractionException("wait for expected text of", description, locator, driver, e);
        }
        return this;
    }

    private WebElement resolveVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new UiInteractionException("locate", description, locator, driver, e);
        }
    }

    private void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }
}
