package com.vgekhtman.automation.common.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class BookingData {

    String firstName;
    String lastName;
    String email;
    String phone;

    @NonNull
    LocalDate checkIn;

    @NonNull
    LocalDate checkOut;

    @NonNull
    RoomType roomType;
}
