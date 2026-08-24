package com.vgekhtman.automation.seleniumframework;

import com.vgekhtman.automation.common.api.BookingApiClient;
import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumframework.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumframework.support.SeleniumExtension;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC-SYNC-001: Synchronization: Booking Submission.
 *
 * <p>Every wait below is a real Selenium explicit wait inside
 * {@link com.vgekhtman.automation.seleniumframework.core.UiElement}, centralized
 * there rather than repeated per Page Object - none of it is a fixed sleep.
 */
@ExtendWith(SeleniumExtension.class)
@Feature("Synchronization")
class SynchronizationTest {

    @Test
    @DisplayName("TC-SYNC-001: booking submission synchronizes on real UI state, not fixed delays")
    void bookingSubmissionSynchronizesOnRealState() {
        BookingData booking = BookingDataFactory.minimalValidBooking();

        RoomReservationPage reservationPage = Allure.step("Open, fill and submit the booking", () -> {
            RoomReservationPage page = new RoomReservationPage()
                    .open(booking.getRoomType(), booking.getCheckIn(), booking.getCheckOut())
                    .startReservation()
                    .fillGuestDetails(booking);
            page.confirmReservation();
            return page;
        });

        Allure.step("Wait for post-submission confirmation without a fixed sleep", () ->
                assertTrue(reservationPage.waitForBookingConfirmed(),
                        "Post-submission confirmation state should be reached without a fixed sleep"));
        BookingApiClient.deleteBooking(booking);
    }
}
