package com.vgekhtman.automation.seleniumbasic.pages.admin;

import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.seleniumbasic.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AdminBookingsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AdminBookingsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.explicitWaitTimeout());
    }

    public AdminBookingsPage openRoomBookings(RoomType roomType) {
        By roomLink = By.id("roomName" + (100 + roomType.getRoomId()));
        wait.until(ExpectedConditions.elementToBeClickable(roomLink)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("room-details")));
        return this;
    }

    // For revisiting a room while already authenticated, where re-running
    // the login flow doesn't apply (no login form is shown once logged in).
    public AdminBookingsPage backToRoomsList() {
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Rooms"))).click();
        return this;
    }

    public boolean hasBookingFor(String firstName) {
        return !driver.findElements(bookingRow(firstName)).isEmpty();
    }

    public AdminBookingsPage waitForBookingVisible(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(bookingRow(firstName)));
        return this;
    }

    public String lastName(String firstName) {
        return rowColumn(firstName, 1).getText();
    }

    public String checkInDate(String firstName) {
        return rowColumn(firstName, 4).getText();
    }

    public String checkOutDate(String firstName) {
        return rowColumn(firstName, 5).getText();
    }

    public AdminBookingsPage editLastName(String firstName, String newLastName) {
        WebElement row = driver.findElement(bookingRow(firstName));
        row.findElement(By.cssSelector(".bookingEdit")).click();

        By lastNameInput = By.cssSelector("input[name='lastname']");
        WebElement input = wait.until(d -> row.findElement(lastNameInput));
        input.clear();
        input.sendKeys(newLastName);
        row.findElement(By.cssSelector(".confirmBookingEdit")).click();

        // Wait on the actual displayed value (re-located fresh each poll),
        // not on the stale `row`/`input` references leaving edit mode.
        wait.until(d -> newLastName.equals(rowColumn(firstName, 1).getText()));
        return this;
    }

    public void deleteBooking(String firstName) {
        driver.findElement(bookingRow(firstName)).findElement(By.cssSelector(".bookingDelete")).click();
        wait.until(d -> d.findElements(bookingRow(firstName)).isEmpty());
    }

    private WebElement rowColumn(String firstName, int columnIndex) {
        WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(bookingRow(firstName)));
        List<WebElement> columns = row.findElement(By.className("row")).findElements(By.xpath("./div"));
        return columns.get(columnIndex);
    }

    private By bookingRow(String firstName) {
        return By.xpath("//div[contains(@class,'detail')]"
                + "[.//p[normalize-space()='" + firstName + "'] or .//input[@name='firstname'][@value='" + firstName + "']]");
    }
}
