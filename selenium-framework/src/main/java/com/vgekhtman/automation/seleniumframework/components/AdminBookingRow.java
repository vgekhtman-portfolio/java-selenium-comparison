package com.vgekhtman.automation.seleniumframework.components;

import com.vgekhtman.automation.seleniumframework.core.UiElement;
import org.openqa.selenium.By;

/**
 * One row of a room's booking table in the Admin panel. The SUT gives each
 * column only a generic Bootstrap grid class (col-sm-N, about layout width,
 * not field identity), so columns are addressed by their fixed position in
 * the row (First name, Last name, Price, Deposit, Check in, Check out) -
 * centralized here rather than repeated wherever a row is read.
 */
public class AdminBookingRow {

    private static final int LAST_NAME_COLUMN = 2;
    private static final int CHECK_IN_COLUMN = 5;
    private static final int CHECK_OUT_COLUMN = 6;

    private final UiElement row;
    private final UiElement lastNameColumn;
    private final UiElement checkInColumn;
    private final UiElement checkOutColumn;
    private final UiElement editIcon;
    private final UiElement deleteIcon;
    private final UiElement confirmEditIcon;
    private final UiElement lastNameInput;

    public AdminBookingRow(String firstName) {
        // The first name renders as a <p> in display mode but as an
        // <input name="firstname" value="..."> once a row enters edit mode
        // (triggered by clicking editIcon below) - match either, so the row
        // stays locatable through the transition instead of only before it.
        String rowXpath = "//div[contains(@class,'detail')]"
                + "[.//p[normalize-space()='" + firstName + "'] or .//input[@name='firstname'][@value='" + firstName + "']]";
        this.row = new UiElement(By.xpath(rowXpath), "booking row for " + firstName);
        this.lastNameColumn = column(rowXpath, LAST_NAME_COLUMN, "last name of " + firstName);
        this.checkInColumn = column(rowXpath, CHECK_IN_COLUMN, "check-in date of " + firstName);
        this.checkOutColumn = column(rowXpath, CHECK_OUT_COLUMN, "check-out date of " + firstName);
        this.editIcon = new UiElement(By.xpath(rowXpath + "//span[contains(@class,'bookingEdit')]"), "edit icon for " + firstName);
        this.deleteIcon = new UiElement(By.xpath(rowXpath + "//span[contains(@class,'bookingDelete')]"), "delete icon for " + firstName);
        this.confirmEditIcon = new UiElement(By.xpath(rowXpath + "//span[contains(@class,'confirmBookingEdit')]"), "confirm-edit icon for " + firstName);
        this.lastNameInput = new UiElement(By.xpath(rowXpath + "//input[@name='lastname']"), "last name input for " + firstName);
    }

    public boolean exists() {
        return row.exists();
    }

    public AdminBookingRow waitUntilVisible() {
        row.waitUntilVisible();
        return this;
    }

    public String lastName() {
        return lastNameColumn.text();
    }

    public String checkInDate() {
        return checkInColumn.text();
    }

    public String checkOutDate() {
        return checkOutColumn.text();
    }

    public AdminBookingRow editLastName(String newLastName) {
        editIcon.click();
        lastNameInput.type(newLastName);
        confirmEditIcon.click();
        lastNameColumn.waitUntilTextEquals(newLastName);
        return this;
    }

    public void delete() {
        deleteIcon.click();
        row.waitUntilGone();
    }

    private static UiElement column(String rowXpath, int position, String description) {
        return new UiElement(By.xpath(rowXpath + "/*[contains(@class,'row')]/div[" + position + "]"), description);
    }
}
