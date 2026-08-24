package com.vgekhtman.automation.seleniumframework;

import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumframework.pages.components.AdminBookingRow;
import com.vgekhtman.automation.seleniumframework.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumframework.pages.admin.AdminLoginPage;
import com.vgekhtman.automation.seleniumframework.support.SeleniumExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-005: Booking Retrieval, via the Admin panel - see the "Retrieval
// Mechanism" note in docs/test-scenarios.md for why.
@ExtendWith(SeleniumExtension.class)
class BookingRetrievalTest {

    @Test
    @DisplayName("TC-BOOK-005: an existing booking is retrieved with the correct data")
    void retrievesExistingBooking() {
        BookingData booking = BookingDataFactory.uniqueValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage()
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();
        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");

        AdminBookingRow row = new AdminLoginPage()
                .open()
                .loginAsAdmin()
                .openRoomBookings(booking.getRoomType())
                .booking(booking.getFirstName())
                .waitUntilVisible();

        assertEquals(booking.getLastName(), row.lastName());
        BookingApiClient.deleteBooking(booking);
    }

    @Test
    @DisplayName("TC-BOOK-005: a non-existing booking is not found")
    void nonExistingBookingIsNotFound() {
        BookingData booking = BookingDataFactory.minimalValidBooking();
        String neverBookedFirstName = "Nobody" + UUID.randomUUID().toString().substring(0, 8);

        AdminBookingRow row = new AdminLoginPage()
                .open()
                .loginAsAdmin()
                .openRoomBookings(booking.getRoomType())
                .booking(neverBookedFirstName);

        assertFalse(row.exists());
    }
}
