package com.vgekhtman.automation.seleniumframework;

import com.vgekhtman.automation.common.model.InvalidBookingCase;
import com.vgekhtman.automation.common.testdata.BookingDataFactory;
import com.vgekhtman.automation.seleniumframework.pages.RoomReservationPage;
import com.vgekhtman.automation.seleniumframework.support.SeleniumExtension;
import io.qameta.allure.Allure;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;

// TC-BOOK-003: Booking Form Validation
@ExtendWith(SeleniumExtension.class)
@Feature("Booking")
class BookingValidationTest {

    static Stream<InvalidBookingCase> invalidBookings() {
        return BookingDataFactory.invalidBookingCases().stream();
    }

    @ParameterizedTest(name = "TC-BOOK-003: rejects {0}")
    @MethodSource("invalidBookings")
    void rejectsInvalidBookingData(InvalidBookingCase invalidCase) {
        RoomReservationPage reservationPage = Allure.step("Open booking page and fill invalid guest details", () ->
                new RoomReservationPage()
                        .open(invalidCase.bookingData().getRoomType(),
                                invalidCase.bookingData().getCheckIn(),
                                invalidCase.bookingData().getCheckOut())
                        .startReservation()
                        .fillGuestDetails(invalidCase.bookingData()));

        Allure.step("Attempt to submit the booking", reservationPage::confirmReservation);

        List<String> validationMessages = Allure.step("Verify validation feedback is shown",
                reservationPage::waitForValidationMessages);
        assertFalse(validationMessages.isEmpty(), "Expected at least one validation message for: " + invalidCase);
    }
}
