package com.vgekhtman.automation.common.model;

public record InvalidBookingCase(String description, BookingData bookingData) {

    @Override
    public String toString() {
        return description;
    }
}
