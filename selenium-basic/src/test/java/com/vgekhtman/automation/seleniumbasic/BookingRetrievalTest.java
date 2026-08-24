package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumbasic.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumbasic.pages.admin.AdminBookingsPage;
import com.vgekhtman.automation.seleniumbasic.pages.admin.AdminLoginPage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-005: Booking Retrieval, via the Admin panel - see the "Retrieval
// Mechanism" note in docs/test-scenarios.md for why.
class BookingRetrievalTest extends BaseUiTest {

    @Test
    @DisplayName("TC-BOOK-005: an existing booking is retrieved with the correct data")
    void retrievesExistingBooking() {
        BookingData booking = BookingDataFactory.uniqueValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage(driver)
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();
        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");

        AdminBookingsPage adminBookings = new AdminLoginPage(driver)
                .open()
                .loginAsAdmin()
                .openRoomBookings(booking.getRoomType());

        adminBookings.waitForBookingVisible(booking.getFirstName());
        assertEquals(booking.getLastName(), adminBookings.lastName(booking.getFirstName()));
        BookingApiClient.deleteBooking(booking);
    }

    @Test
    @DisplayName("TC-BOOK-005: a non-existing booking is not found")
    void nonExistingBookingIsNotFound() {
        BookingData booking = BookingDataFactory.minimalValidBooking();
        String neverBookedFirstName = "Nobody" + UUID.randomUUID().toString().substring(0, 8);

        AdminBookingsPage adminBookings = new AdminLoginPage(driver)
                .open()
                .loginAsAdmin()
                .openRoomBookings(booking.getRoomType());

        assertFalse(adminBookings.hasBookingFor(neverBookedFirstName));
    }
}
