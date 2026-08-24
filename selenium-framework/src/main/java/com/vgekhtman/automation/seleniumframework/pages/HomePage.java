package com.vgekhtman.automation.seleniumframework.pages;

import com.vgekhtman.automation.common.config.Config;
import com.vgekhtman.automation.seleniumframework.core.UiElement;
import com.vgekhtman.automation.seleniumframework.driver.DriverManager;
import org.openqa.selenium.By;

public class HomePage {

    private final UiElement roomsHeading;
    private final UiElement bookNowLinks;
    private final UiElement adminLink;

    public HomePage() {
        this.roomsHeading = new UiElement(By.xpath("//h2[normalize-space()='Our Rooms']"), "rooms section heading");
        this.bookNowLinks = new UiElement(By.xpath("//a[normalize-space()='Book now']"), "room 'Book now' links");
        this.adminLink = new UiElement(By.linkText("Admin"), "Admin nav link");
    }

    public HomePage open() {
        DriverManager.getDriver().get(Config.baseUrl());
        roomsHeading.waitUntilVisible();
        return this;
    }

    public String pageTitle() {
        return DriverManager.getDriver().getTitle();
    }

    public String currentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    public boolean isRoomsSectionDisplayed() {
        return roomsHeading.isDisplayed();
    }

    public boolean areBookNowControlsActionable() {
        return bookNowLinks.allDisplayedAndEnabled();
    }

    public void openAdminPanel() {
        adminLink.click();
    }
}
