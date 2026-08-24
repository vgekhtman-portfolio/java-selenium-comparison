package com.vgekhtman.automation.seleniumframework.pages;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.seleniumframework.components.BookingForm;
import com.vgekhtman.automation.seleniumframework.config.FrameworkConfig;
import com.vgekhtman.automation.seleniumframework.core.UiElement;
import com.vgekhtman.automation.seleniumframework.driver.DriverManager;
import org.openqa.selenium.By;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RoomReservationPage {

    private final BookingForm bookingForm;
    private final UiElement confirmationHeading;
    private final UiElement confirmationCard;

    public RoomReservationPage() {
        this.bookingForm = new BookingForm();
        this.confirmationHeading = new UiElement(By.xpath("//h2[normalize-space()='Booking Confirmed']"), "booking confirmation heading");
        this.confirmationCard = new UiElement(By.className("booking-card"), "booking confirmation card");
    }

    public RoomReservationPage open(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        String url = "%s/reservation/%d?checkin=%s&checkout=%s".formatted(
                FrameworkConfig.baseUrl(),
                roomType.getRoomId(),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkIn),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkOut));
        DriverManager.getDriver().get(url);
        bookingForm.waitUntilReady();
        return this;
    }

    /**
     * Reveals the guest-details form. The fields do not exist in the DOM
     * until this click resolves, so this is a genuine wait point rather
     * than a formality.
     */
    public RoomReservationPage startReservation() {
        bookingForm.submit();
        bookingForm.waitUntilFieldsVisible();
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
        confirmationHeading.waitUntilVisible();
        return true;
    }

    public String confirmationDetailsText() {
        return confirmationCard.text();
    }

    public List<String> waitForValidationMessages() {
        return bookingForm.waitForValidationMessages();
    }
}
