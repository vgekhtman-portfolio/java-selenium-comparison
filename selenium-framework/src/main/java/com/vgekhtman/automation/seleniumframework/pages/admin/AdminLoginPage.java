package com.vgekhtman.automation.seleniumframework.pages.admin;

import com.vgekhtman.automation.seleniumframework.config.FrameworkConfig;
import com.vgekhtman.automation.seleniumframework.core.UiElement;
import com.vgekhtman.automation.seleniumframework.driver.DriverManager;
import org.openqa.selenium.By;

public class AdminLoginPage {

    private final UiElement usernameInput;
    private final UiElement passwordInput;
    private final UiElement loginButton;
    private final UiElement roomsNavLink;

    public AdminLoginPage() {
        this.usernameInput = new UiElement(By.id("username"), "admin username field");
        this.passwordInput = new UiElement(By.id("password"), "admin password field");
        this.loginButton = new UiElement(By.xpath("//button[normalize-space()='Login']"), "admin login button");
        this.roomsNavLink = new UiElement(By.linkText("Rooms"), "admin Rooms nav link");
    }

    public AdminLoginPage open() {
        DriverManager.getDriver().get(FrameworkConfig.baseUrl() + "/admin");
        usernameInput.waitUntilVisible();
        return this;
    }

    public AdminBookingsPage loginAsAdmin() {
        usernameInput.type(FrameworkConfig.adminUsername());
        passwordInput.type(FrameworkConfig.adminPassword());
        loginButton.click();
        roomsNavLink.waitUntilVisible();
        return new AdminBookingsPage();
    }
}
