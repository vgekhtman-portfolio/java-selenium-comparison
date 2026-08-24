package com.vgekhtman.automation.seleniumframework;

import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumframework.pages.components.AdminBookingRow;
import com.vgekhtman.automation.seleniumframework.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumframework.pages.admin.AdminBookingsPage;
import com.vgekhtman.automation.seleniumframework.pages.admin.AdminLoginPage;
import com.vgekhtman.automation.seleniumframework.support.SeleniumExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-006: State Persistence
@ExtendWith(SeleniumExtension.class)
class StatePersistenceTest {

    @Test
    @DisplayName("TC-BOOK-006: booking state is unchanged after revisiting the admin view")
    void bookingStateRemainsConsistentAcrossRevisit() {
        BookingData booking = BookingDataFactory.uniqueValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage()
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();
        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");

        AdminBookingsPage adminBookings = new AdminLoginPage()
                .open()
                .loginAsAdmin()
                .openRoomBookings(booking.getRoomType());
        AdminBookingRow firstVisitRow = adminBookings.booking(booking.getFirstName()).waitUntilVisible();
        assertEquals(booking.getLastName(), firstVisitRow.lastName());

        AdminBookingRow revisitRow = adminBookings.backToRoomsList()
                .openRoomBookings(booking.getRoomType())
                .booking(booking.getFirstName())
                .waitUntilVisible();

        assertEquals(booking.getLastName(), revisitRow.lastName(), "Last name should be unchanged after revisiting");
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckIn()), revisitRow.checkInDate(),
                "Check-in date should be unchanged after revisiting");
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckOut()), revisitRow.checkOutDate(),
                "Check-out date should be unchanged after revisiting");
        BookingApiClient.deleteBooking(booking);
    }
}
