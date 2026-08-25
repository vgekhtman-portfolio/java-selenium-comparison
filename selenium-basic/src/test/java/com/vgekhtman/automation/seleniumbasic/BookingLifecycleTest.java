package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumbasic.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumbasic.pages.admin.AdminBookingsPage;
import com.vgekhtman.automation.seleniumbasic.pages.admin.AdminLoginPage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// TC-BOOK-004: Booking Lifecycle
@Feature("Booking")
class BookingLifecycleTest extends BaseUiTest {

    @Test
    @DisplayName("TC-BOOK-004: booking can be created, updated and deleted through its full lifecycle")
    void bookingLifecycle() {
        // Data is unique per run - this test manages its own booking end to
        // end and must not depend on, or interfere with, data left by any
        // other test or by other users of this shared public demo instance.
        BookingData booking = BookingDataFactory.uniqueValidBooking();

        RoomReservationPage reservationPage = Allure.step("Create a booking through the UI", () ->
                new RoomReservationPage(driver)
                        .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                        .startReservation()
                        .fillGuestDetails(booking));
        reservationPage.confirmReservation();
        assertTrue(reservationPage.waitForBookingConfirmed(), "Booking should be confirmed");

        AdminBookingsPage adminBookings = Allure.step("Retrieve the booking via the Admin panel", () ->
                new AdminLoginPage(driver)
                        .open()
                        .loginAsAdmin()
                        .openRoomBookings(booking.getRoomType()));
        adminBookings.waitForBookingVisible(booking.getFirstName());

        Allure.step("Verify initial booking details", () -> {
            assertEquals(booking.getLastName(), adminBookings.lastName(booking.getFirstName()));
            assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckIn()),
                    adminBookings.checkInDate(booking.getFirstName()));
            assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(booking.getCheckOut()),
                    adminBookings.checkOutDate(booking.getFirstName()));
        });

        String updatedLastName = booking.getLastName() + "Updated";
        Allure.step("Update the booking's last name", () -> {
            adminBookings.editLastName(booking.getFirstName(), updatedLastName);
        });
        assertEquals(updatedLastName, adminBookings.lastName(booking.getFirstName()),
                "Last name should reflect the update");

        Allure.step("Delete the booking", () -> adminBookings.deleteBooking(booking.getFirstName()));
        assertFalse(adminBookings.hasBookingFor(booking.getFirstName()),
                "Booking should no longer be listed after deletion");
    }
}
