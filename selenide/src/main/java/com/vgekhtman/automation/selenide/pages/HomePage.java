package com.vgekhtman.automation.selenide.pages;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.WebDriverRunner.url;

public class HomePage {

    private final SelenideElement roomsHeading = $x("//h2[normalize-space()='Our Rooms']");
    private final ElementsCollection bookNowLinks = $$x("//a[normalize-space()='Book now']");
    private final SelenideElement adminLink = $(By.linkText("Admin"));

    public HomePage open() {
        Selenide.open("/");
        roomsHeading.shouldBe(Condition.visible);
        return this;
    }

    public String pageTitle() {
        return title();
    }

    public String currentUrl() {
        return url();
    }

    public boolean isRoomsSectionDisplayed() {
        return roomsHeading.isDisplayed();
    }

    public boolean areBookNowControlsActionable() {
        try {
            bookNowLinks.shouldBe(CollectionCondition.allMatch("displayed and enabled",
                    element -> element.isDisplayed() && element.isEnabled()));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public void openAdminPanel() {
        adminLink.click();
    }
}
