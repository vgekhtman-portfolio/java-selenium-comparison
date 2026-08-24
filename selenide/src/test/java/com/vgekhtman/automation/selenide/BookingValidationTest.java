package com.vgekhtman.automation.selenide;

import com.vgekhtman.automation.selenide.support.SelenideExtension;
import com.vgekhtman.automation.common.model.InvalidBookingCase;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.selenide.pages.RoomReservationPage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

// TC-BOOK-003: Booking Form Validation
@ExtendWith(SelenideExtension.class)
class BookingValidationTest {

    static Stream<InvalidBookingCase> invalidBookings() {
        return BookingDataFactory.invalidBookingCases().stream();
    }

    @ParameterizedTest(name = "TC-BOOK-003: rejects {0}")
    @MethodSource("invalidBookings")
    void rejectsInvalidBookingData(InvalidBookingCase invalidCase) {
        RoomReservationPage reservationPage = new RoomReservationPage()
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
