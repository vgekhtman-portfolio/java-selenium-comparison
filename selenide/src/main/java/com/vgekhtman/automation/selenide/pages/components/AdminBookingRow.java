package com.vgekhtman.automation.selenide.pages.components;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

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

    private final SelenideElement row;
    private final SelenideElement lastNameColumn;
    private final SelenideElement checkInColumn;
    private final SelenideElement checkOutColumn;
    private final SelenideElement editIcon;
    private final SelenideElement deleteIcon;
    private final SelenideElement confirmEditIcon;
    private final SelenideElement lastNameInput;

    public AdminBookingRow(String firstName) {
        // The first name renders as a <p> in display mode but as an
        // <input name="firstname" value="..."> once a row enters edit mode
        // (triggered by clicking editIcon below) - match either, so the row
        // stays locatable through the transition instead of only before it.
        String rowXpath = "//div[contains(@class,'detail')]"
                + "[.//p[normalize-space()='" + firstName + "'] or .//input[@name='firstname'][@value='" + firstName + "']]";
        this.row = $x(rowXpath);
        this.lastNameColumn = column(rowXpath, LAST_NAME_COLUMN);
        this.checkInColumn = column(rowXpath, CHECK_IN_COLUMN);
        this.checkOutColumn = column(rowXpath, CHECK_OUT_COLUMN);
        this.editIcon = $x(rowXpath + "//span[contains(@class,'bookingEdit')]");
        this.deleteIcon = $x(rowXpath + "//span[contains(@class,'bookingDelete')]");
        this.confirmEditIcon = $x(rowXpath + "//span[contains(@class,'confirmBookingEdit')]");
        this.lastNameInput = $x(rowXpath + "//input[@name='lastname']");
    }

    public boolean exists() {
        return row.exists();
    }

    public AdminBookingRow waitUntilVisible() {
        row.shouldBe(Condition.visible);
        return this;
    }

    public String lastName() {
        return lastNameColumn.getText();
    }

    public String checkInDate() {
        return checkInColumn.getText();
    }

    public String checkOutDate() {
        return checkOutColumn.getText();
    }

    public AdminBookingRow editLastName(String newLastName) {
        editIcon.click();
        lastNameInput.setValue(newLastName);
        confirmEditIcon.click();
        lastNameColumn.shouldHave(Condition.exactText(newLastName));
        return this;
    }

    public void delete() {
        deleteIcon.click();
        row.shouldNotBe(Condition.exist);
    }

    private static SelenideElement column(String rowXpath, int position) {
        return $x(rowXpath + "/*[contains(@class,'row')]/div[" + position + "]");
    }
}
