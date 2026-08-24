package com.vgekhtman.automation.seleniumbasic.pages.admin;

import com.vgekhtman.automation.seleniumbasic.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminLoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameInput = By.id("username");
    private final By passwordInput = By.id("password");
    private final By loginButton = By.xpath("//button[normalize-space()='Login']");

    public AdminLoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.explicitWaitTimeout());
    }

    public AdminLoginPage open() {
        driver.get(TestConfig.baseUrl() + "/admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        return this;
    }

    public AdminBookingsPage loginAsAdmin() {
        driver.findElement(usernameInput).sendKeys(TestConfig.adminUsername());
        driver.findElement(passwordInput).sendKeys(TestConfig.adminPassword());
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Rooms")));
        return new AdminBookingsPage(driver);
    }
}
