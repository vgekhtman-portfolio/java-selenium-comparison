package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumbasic.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC-SYNC-001: Synchronization: Booking Submission.
 *
 * <p>Every wait below is a real Selenium explicit wait inside
 * {@link RoomReservationPage} - none of it is a fixed sleep. The two
 * genuine synchronization points on this SUT are: the guest-details form
 * does not exist in the DOM until the first "Reserve Now" click resolves
 * (open -> startReservation), and the confirmation state is rendered
 * asynchronously after submission (waitForBookingConfirmed).
 */
class SynchronizationTest extends BaseUiTest {

    @Test
    @DisplayName("TC-SYNC-001: booking submission synchronizes on real UI state, not fixed delays")
    void bookingSubmissionSynchronizesOnRealState() {
        BookingData booking = BookingDataFactory.minimalValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage(driver)
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();

        assertTrue(reservationPage.waitForBookingConfirmed(),
                "Post-submission confirmation state should be reached without a fixed sleep");
    }
}
