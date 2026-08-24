package com.vgekhtman.automation.seleniumbasic;

import com.vgekhtman.automation.common.model.InvalidBookingCase;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumbasic.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumbasic.support.BaseUiTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

// TC-BOOK-003: Booking Form Validation
class BookingValidationTest extends BaseUiTest {

    static Stream<InvalidBookingCase> invalidBookings() {
        return BookingDataFactory.invalidBookingCases().stream();
    }

    @ParameterizedTest(name = "TC-BOOK-003: rejects {0}")
    @MethodSource("invalidBookings")
    void rejectsInvalidBookingData(InvalidBookingCase invalidCase) {
        RoomReservationPage reservationPage = new RoomReservationPage(driver)
                .open(invalidCase.bookingData().getRoomType(),
                        invalidCase.bookingData().getCheckIn(),
                        invalidCase.bookingData().getCheckOut())
                .startReservation()
                .fillGuestDetails(invalidCase.bookingData());
        reservationPage.confirmReservation();

        List<String> validationMessages = reservationPage.waitForValidationMessages();
        assertFalse(validationMessages.isEmpty(), "Expected at least one validation message for: " + invalidCase);
    }
}
