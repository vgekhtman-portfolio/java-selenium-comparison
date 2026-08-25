package com.vgekhtman.automation.seleniumframework.pages.components;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.seleniumframework.core.UiElement;
import org.openqa.selenium.By;

import java.util.List;

/**
 * The guest-details booking card on a room reservation page. Its primary
 * button serves double duty on this SUT: the first click reveals the guest
 * fields, and the same button/locator submits them once filled - so
 * {@link #submit()} is meaningful to call at either point.
 */
public class BookingForm {

    private final UiElement firstNameInput;
    private final UiElement lastNameInput;
    private final UiElement emailInput;
    private final UiElement phoneInput;
    private final UiElement submitButton;
    private final UiElement validationMessages;

    public BookingForm() {
        this.firstNameInput = new UiElement(By.cssSelector("input.room-firstname"), "booking form firstname field");
        this.lastNameInput = new UiElement(By.cssSelector("input.room-lastname"), "booking form lastname field");
        this.emailInput = new UiElement(By.cssSelector("input.room-email"), "booking form email field");
        this.phoneInput = new UiElement(By.cssSelector("input.room-phone"), "booking form phone field");
        this.submitButton = new UiElement(By.xpath("//button[normalize-space()='Reserve Now']"), "booking form submit button");
        this.validationMessages = new UiElement(By.cssSelector(".alert-danger li"), "booking form validation messages");
    }

    public BookingForm waitUntilReady() {
        submitButton.waitUntilClickable();
        return this;
    }

    public BookingForm fillGuestDetails(BookingData bookingData) {
        firstNameInput.type(bookingData.getFirstName());
        lastNameInput.type(bookingData.getLastName());
        emailInput.type(bookingData.getEmail());
        phoneInput.type(bookingData.getPhone());
        return this;
    }

    public BookingForm waitUntilFieldsVisible() {
        firstNameInput.waitUntilVisible();
        return this;
    }

    public void submit() {
        submitButton.click();
    }

    public List<String> waitForValidationMessages() {
        return validationMessages.texts();
    }
}
