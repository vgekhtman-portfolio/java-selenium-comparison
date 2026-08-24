package com.vgekhtman.automation.selenide.components;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.vgekhtman.automation.common.model.BookingData;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.$x;

/**
 * The guest-details booking card on a room reservation page. Its primary
 * button serves double duty on this SUT: the first click reveals the guest
 * fields, and the same button/locator submits them once filled - so
 * {@link #submit()} is meaningful to call at either point.
 */
public class BookingForm {

    private final SelenideElement firstNameInput = $("input.room-firstname");
    private final SelenideElement lastNameInput = $("input.room-lastname");
    private final SelenideElement emailInput = $("input.room-email");
    private final SelenideElement phoneInput = $("input.room-phone");
    private final SelenideElement submitButton = $x("//button[normalize-space()='Reserve Now']");
    private final ElementsCollection validationMessages = $$(".alert-danger li");

    public BookingForm fillGuestDetails(BookingData bookingData) {
        firstNameInput.setValue(bookingData.getFirstName());
        lastNameInput.setValue(bookingData.getLastName());
        emailInput.setValue(bookingData.getEmail());
        phoneInput.setValue(bookingData.getPhone());
        return this;
    }

    // No explicit wait needed before or after - setValue()/click() already
    // wait for their target to become visible/interactable.
    public void submit() {
        submitButton.click();
    }

    public List<String> waitForValidationMessages() {
        return validationMessages.shouldHave(CollectionCondition.sizeGreaterThan(0)).texts();
    }
}
