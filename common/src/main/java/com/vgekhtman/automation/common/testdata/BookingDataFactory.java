package com.vgekhtman.automation.common.testdata;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.model.InvalidBookingCase;
import com.vgekhtman.automation.common.model.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * The live booking form has no optional fields - firstname, lastname, email
 * and phone are always required. So "minimal" vs "complete" here means value
 * variety (shortest realistic values vs. near-upper-bound values, a longer
 * stay, a different room), not field presence.
 */
public final class BookingDataFactory {

    private BookingDataFactory() {
    }

    /** Shortest realistic values, single night, cheapest room. */
    public static BookingData minimalValidBooking() {
        return BookingData.builder()
                .firstName("Amy")
                .lastName("Lee")
                .email("amy.lee@example.com")
                .phone("01234567890")
                .checkIn(LocalDate.now().plusDays(1))
                .checkOut(LocalDate.now().plusDays(2))
                .roomType(RoomType.SINGLE)
                .build();
    }

    /** Near-upper-bound values, multi-night stay, different room. */
    public static BookingData completeValidBooking() {
        return BookingData.builder()
                .firstName("Wolfgang-Amadeus")
                .lastName("Livingstone-Rutherford-Wells")
                .email("wolfgang.amadeus.livingstone@example.com")
                .phone("01123456789012345678")
                .checkIn(LocalDate.now().plusDays(3))
                .checkOut(LocalDate.now().plusDays(8))
                .roomType(RoomType.SUITE)
                .build();
    }

    /**
     * A valid booking with a unique name/email suffix, safe to use where tests
     * run in parallel or must avoid colliding with data left by earlier runs.
     */
    public static BookingData uniqueValidBooking() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        return minimalValidBooking().toBuilder()
                .lastName("Doe" + suffix)
                .email("jane.doe+" + suffix + "@example.com")
                .build();
    }

    // Bounds below come from the field validation messages the live SUT
    // actually returns (firstname 3-18, lastname 3-30, phone 11-21, email
    // required only) - not arbitrary values.
    public static List<InvalidBookingCase> invalidBookingCases() {
        BookingData valid = minimalValidBooking();
        return List.of(
                new InvalidBookingCase("blank firstname", valid.toBuilder().firstName("").build()),
                new InvalidBookingCase("firstname shorter than 3 characters", valid.toBuilder().firstName("Al").build()),
                new InvalidBookingCase("firstname longer than 18 characters", valid.toBuilder().firstName("A".repeat(19)).build()),
                new InvalidBookingCase("blank lastname", valid.toBuilder().lastName("").build()),
                new InvalidBookingCase("lastname shorter than 3 characters", valid.toBuilder().lastName("Al").build()),
                new InvalidBookingCase("lastname longer than 30 characters", valid.toBuilder().lastName("A".repeat(31)).build()),
                new InvalidBookingCase("blank email", valid.toBuilder().email("").build()),
                new InvalidBookingCase("blank phone", valid.toBuilder().phone("").build()),
                new InvalidBookingCase("phone shorter than 11 characters", valid.toBuilder().phone("123456789").build()),
                new InvalidBookingCase("phone longer than 21 characters", valid.toBuilder().phone("1".repeat(22)).build()));
    }
}
