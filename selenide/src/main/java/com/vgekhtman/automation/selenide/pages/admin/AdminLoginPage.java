package com.vgekhtman.automation.selenide.pages.admin;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.vgekhtman.automation.selenide.config.SelenideConfig;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class AdminLoginPage {

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $x("//button[normalize-space()='Login']");
    private final SelenideElement roomsNavLink = $(By.linkText("Rooms"));

    public AdminLoginPage open() {
        Selenide.open("/admin");
        usernameInput.shouldBe(Condition.visible);
        return this;
    }

    public AdminBookingsPage loginAsAdmin() {
        usernameInput.setValue(SelenideConfig.adminUsername());
        passwordInput.setValue(SelenideConfig.adminPassword());
        loginButton.click();
        roomsNavLink.shouldBe(Condition.visible);
        return new AdminBookingsPage();
    }
}
