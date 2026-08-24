package com.vgekhtman.automation.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoomType {

    SINGLE(1, "Single", 100),
    DOUBLE(2, "Double", 150),
    SUITE(3, "Suite", 225);

    private final int roomId;
    private final String displayName;
    private final int pricePerNight;
}
