package com.vgekhtman.automation.seleniumframework;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumframework.pages.components.AdminBookingRow;
import com.vgekhtman.automation.seleniumframework.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumframework.pages.admin.AdminLoginPage;
import com.vgekhtman.automation.seleniumframework.support.SeleniumExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-004: Booking Lifecycle
@ExtendWith(SeleniumExtension.class)
class BookingLifecycleTest {

    @Test
    @DisplayName("TC-BOOK-004: booking can be created, updated and deleted through its full lifecycle")
    void bookingLifecycle() {
        // Data is unique per run - this test manages its own booking end to
        // end and must not depend on, or interfere with, data left by any
        // other test or by other users of this shared public demo instance.
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
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckIn()), row.checkInDate());
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckOut()), row.checkOutDate());

        String updatedLastName = booking.getLastName() + "Updated";
        row.editLastName(updatedLastName);
        assertEquals(updatedLastName, row.lastName(), "Last name should reflect the update");

        row.delete();
        assertFalse(row.exists(), "Booking should no longer be listed after deletion");
    }
}
