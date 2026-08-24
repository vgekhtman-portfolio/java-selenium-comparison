package com.vgekhtman.automation.selenide;

import com.vgekhtman.automation.selenide.support.SelenideExtension;
import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.selenide.pages.RoomReservationPage;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC-SYNC-001: Synchronization: Booking Submission.
 *
 * <p>No explicit waits appear anywhere in this flow - every Selenide action
 * (click(), setValue(), shouldBe()) already waits for its target's real
 * state before acting or asserting. None of it is a fixed sleep.
 */
@ExtendWith(SelenideExtension.class)
@Feature("Synchronization")
class SynchronizationTest {

    @Test
    @DisplayName("TC-SYNC-001: booking submission synchronizes on real UI state, not fixed delays")
    void bookingSubmissionSynchronizesOnRealState() {
        BookingData booking = BookingDataFactory.minimalValidBooking();

        RoomReservationPage reservationPage = new RoomReservationPage()
                .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                .startReservation()
                .fillGuestDetails(booking);
        reservationPage.confirmReservation();

        assertTrue(reservationPage.waitForBookingConfirmed(),
                "Post-submission confirmation state should be reached without a fixed sleep");
        BookingApiClient.deleteBooking(booking);
    }
}
