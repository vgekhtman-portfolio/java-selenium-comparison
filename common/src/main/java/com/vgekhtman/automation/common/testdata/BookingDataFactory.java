package com.vgekhtman.automation.common.testdata;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.model.InvalidBookingCase;
import com.vgekhtman.automation.common.model.RoomType;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The live booking form has no optional fields - firstname, lastname, email
 * and phone are always required. So "minimal" vs "complete" here means value
 * variety (shortest realistic values vs. near-upper-bound values, a longer
 * stay, a different room), not field presence.
 *
 * <p>Stay dates are randomized within a wide future window rather than
 * fixed. The SUT enforces real room+date conflicts server-side (a 409 that
 * the frontend does not handle gracefully) even though its calendar UI
 * never visually shows a date as unavailable - a fixed "tomorrow" date
 * collides with itself, and with other users of this shared public demo
 * instance, after the first successful booking for that exact slot.
 */
public final class BookingDataFactory {

    private static final int MIN_DAYS_OUT = 1;
    private static final int MAX_DAYS_OUT = 3650;

    private BookingDataFactory() {
    }

    /** Shortest realistic values, single night, cheapest room. */
    public static BookingData minimalValidBooking() {
        LocalDate checkIn = randomFutureDate();
        return BookingData.builder()
                .firstName("Amy")
                .lastName("Lee")
                .email("amy.lee@example.com")
                .phone("01234567890")
                .checkIn(checkIn)
                .checkOut(checkIn.plusDays(1))
                .roomType(RoomType.SINGLE)
                .build();
    }

    /** Near-upper-bound values, multi-night stay, different room. */
    public static BookingData completeValidBooking() {
        LocalDate checkIn = randomFutureDate();
        return BookingData.builder()
                .firstName("Wolfgang-Amadeus")
                .lastName("Livingstone-Rutherford-Wells")
                .email("wolfgang.amadeus.livingstone@example.com")
                .phone("01123456789012345678")
                .checkIn(checkIn)
                .checkOut(checkIn.plusDays(5))
                .roomType(RoomType.SUITE)
                .build();
    }

    private static LocalDate randomFutureDate() {
        return LocalDate.now().plusDays(ThreadLocalRandom.current().nextInt(MIN_DAYS_OUT, MAX_DAYS_OUT + 1));
    }

    /**
     * A valid booking with a unique first name, last name and email, safe to
     * use where tests run in parallel or must avoid colliding with data left
     * by earlier runs - including admin-panel lookups that key off first
     * name alone, which a non-unique first name would collide on across
     * repeated executions even with a unique last name/email.
     */
    public static BookingData uniqueValidBooking() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        return minimalValidBooking().toBuilder()
                .firstName("Amy" + suffix)
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
