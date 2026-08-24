package com.vgekhtman.automation.seleniumframework.pages.admin;

import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.seleniumframework.components.AdminBookingRow;
import com.vgekhtman.automation.seleniumframework.core.UiElement;
import org.openqa.selenium.By;

public class AdminBookingsPage {

    public AdminBookingsPage openRoomBookings(RoomType roomType) {
        UiElement roomLink = new UiElement(By.id("roomName" + (100 + roomType.getRoomId())), "room link for " + roomType);
        roomLink.click();
        new UiElement(By.className("room-details"), "room details section").waitUntilVisible();
        return this;
    }

    // For revisiting a room while already authenticated, where re-running
    // the login flow doesn't apply (no login form is shown once logged in).
    public AdminBookingsPage backToRoomsList() {
        new UiElement(By.linkText("Rooms"), "admin Rooms nav link").click();
        return this;
    }

    public AdminBookingRow booking(String firstName) {
        return new AdminBookingRow(firstName);
    }
}
