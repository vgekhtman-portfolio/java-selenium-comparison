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

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-006: State Persistence
class StatePersistenceTest extends BaseUiTest {

    @Test
    @DisplayName("TC-BOOK-006: booking state is unchanged after revisiting the admin view")
    void bookingStateRemainsConsistentAcrossRevisit() {
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

        adminBookings.backToRoomsList().openRoomBookings(booking.getRoomType());
        adminBookings.waitForBookingVisible(booking.getFirstName());

        assertEquals(booking.getLastName(), adminBookings.lastName(booking.getFirstName()),
                "Last name should be unchanged after revisiting");
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckIn()),
                adminBookings.checkInDate(booking.getFirstName()),
                "Check-in date should be unchanged after revisiting");
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckOut()),
                adminBookings.checkOutDate(booking.getFirstName()),
                "Check-out date should be unchanged after revisiting");
        BookingApiClient.deleteBooking(booking);
    }
}
