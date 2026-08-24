package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumbasic.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateBookingTest extends BaseUiTest {

    @Test
    @DisplayName("TC-BOOK-001: create booking with minimal valid data")
    void createsBookingWithMinimalValidData() {
        BookingData booking = BookingDataFactory.minimalValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage(driver)
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();

        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");
        assertConfirmationShowsDates(reservationPage, booking);
        BookingApiClient.deleteBooking(booking);
    }

    @Test
    @DisplayName("TC-BOOK-002: create booking with complete data")
    void createsBookingWithCompleteData() {
        BookingData booking = BookingDataFactory.completeValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage(driver)
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();

        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");
        assertConfirmationShowsDates(reservationPage, booking);
        BookingApiClient.deleteBooking(booking);
    }

    private void assertConfirmationShowsDates(RoomReservationPage reservationPage, BookingData booking) {
        String details = reservationPage.confirmationDetailsText();
        assertTrue(details.contains(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckIn())),
                "Confirmation should show the check-in date: " + details);
        assertTrue(details.contains(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckOut())),
                "Confirmation should show the check-out date: " + details);
    }
}
