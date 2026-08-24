package com.vgekhtman.automation.selenide.pages.admin;

import com.codeborne.selenide.Condition;
import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.selenide.components.AdminBookingRow;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class AdminBookingsPage {

    public AdminBookingsPage openRoomBookings(RoomType roomType) {
        $("#roomName" + (100 + roomType.getRoomId())).click();
        $(".room-details").shouldBe(Condition.visible);
        return this;
    }

    // For revisiting a room while already authenticated, where re-running
    // the login flow doesn't apply (no login form is shown once logged in).
    public AdminBookingsPage backToRoomsList() {
        $(By.linkText("Rooms")).click();
        return this;
    }

    public AdminBookingRow booking(String firstName) {
        return new AdminBookingRow(firstName);
    }
}
