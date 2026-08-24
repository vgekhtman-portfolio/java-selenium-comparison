package com.vgekhtman.automation.selenide.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.selenide.pages.components.BookingForm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RoomReservationPage {

    private final BookingForm bookingForm = new BookingForm();
    private final SelenideElement confirmationHeading = $x("//h2[normalize-space()='Booking Confirmed']");
    private final SelenideElement confirmationCard = $(".booking-card");

    public RoomReservationPage open(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        String url = "/reservation/%d?checkin=%s&checkout=%s".formatted(
                roomType.getRoomId(),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkIn),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkOut));
        Selenide.open(url);
        return this;
    }

    /**
     * Reveals the guest-details form. The fields do not exist in the DOM
     * until this click resolves - Selenide's click() already waits for
     * that, so there is nothing extra to wait for here.
     */
    public RoomReservationPage startReservation() {
        bookingForm.submit();
        return this;
    }

    public RoomReservationPage fillGuestDetails(BookingData bookingData) {
        bookingForm.fillGuestDetails(bookingData);
        return this;
    }

    public void confirmReservation() {
        bookingForm.submit();
    }

    public boolean waitForBookingConfirmed() {
        confirmationHeading.shouldBe(Condition.visible);
        return true;
    }

    public String confirmationDetailsText() {
        return confirmationCard.getText();
    }

    public List<String> waitForValidationMessages() {
        return bookingForm.waitForValidationMessages();
    }
}
