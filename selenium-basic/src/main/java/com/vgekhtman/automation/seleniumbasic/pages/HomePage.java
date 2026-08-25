package com.vgekhtman.automation.seleniumbasic.pages;

import com.vgekhtman.automation.common.config.Config;
import com.vgekhtman.automation.seleniumbasic.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By roomsHeading = By.xpath("//h2[normalize-space()='Our Rooms']");
    private final By bookNowLinks = By.xpath("//a[normalize-space()='Book now']");
    private final By adminLink = By.linkText("Admin");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.explicitWaitTimeout());
    }

    public HomePage open() {
        driver.get(Config.baseUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(roomsHeading));
        return this;
    }

    public String pageTitle() {
        return driver.getTitle();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public boolean isRoomsSectionDisplayed() {
        return driver.findElement(roomsHeading).isDisplayed();
    }

    public boolean areBookNowControlsActionable() {
        return driver.findElements(bookNowLinks).stream().allMatch(link -> link.isDisplayed() && link.isEnabled());
    }

    public void openAdminPanel() {
        wait.until(ExpectedConditions.elementToBeClickable(adminLink)).click();
    }
}
