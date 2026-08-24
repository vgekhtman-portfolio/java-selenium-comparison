package com.vgekhtman.automation.seleniumbasic.pages;

import com.vgekhtman.automation.common.model.BookingData;
import com.vgekhtman.automation.common.model.RoomType;
import com.vgekhtman.automation.seleniumbasic.config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RoomReservationPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstNameInput = By.cssSelector("input.room-firstname");
    private final By lastNameInput = By.cssSelector("input.room-lastname");
    private final By emailInput = By.cssSelector("input.room-email");
    private final By phoneInput = By.cssSelector("input.room-phone");
    private final By reserveNowButton = By.xpath("//button[normalize-space()='Reserve Now']");
    private final By validationMessages = By.cssSelector(".alert-danger li");
    private final By confirmationHeading = By.xpath("//h2[normalize-space()='Booking Confirmed']");
    private final By confirmationCard = By.className("booking-card");

    public RoomReservationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.explicitWaitTimeout());
    }

    public RoomReservationPage open(RoomType roomType, LocalDate checkIn, LocalDate checkOut) {
        String url = "%s/reservation/%d?checkin=%s&checkout=%s".formatted(
                TestConfig.baseUrl(),
                roomType.getRoomId(),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkIn),
                DateTimeFormatter.ISO_LOCAL_DATE.format(checkOut));
        driver.get(url);
        wait.until(ExpectedConditions.elementToBeClickable(reserveNowButton));
        return this;
    }

    /**
     * Reveals the guest-details form. The fields do not exist in the DOM
     * until this click resolves, so this is a genuine wait point rather
     * than a formality.
     */
    public RoomReservationPage startReservation() {
        clickReserveNow();
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        return this;
    }

    public RoomReservationPage fillGuestDetails(BookingData bookingData) {
        fillField(firstNameInput, bookingData.getFirstName());
        fillField(lastNameInput, bookingData.getLastName());
        fillField(emailInput, bookingData.getEmail());
        fillField(phoneInput, bookingData.getPhone());
        return this;
    }

    public void confirmReservation() {
        clickReserveNow();
    }

    // The button sits at the bottom edge of the booking card, so Chrome
    // sometimes reports it as not clickable at its native point until it is
    // scrolled fully into view first.
    private void clickReserveNow() {
        WebElement button = driver.findElement(reserveNowButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        wait.until(ExpectedConditions.elementToBeClickable(reserveNowButton)).click();
    }

    public boolean waitForBookingConfirmed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationHeading)) != null;
    }

    public String confirmationDetailsText() {
        return driver.findElement(confirmationCard).getText();
    }

    public List<String> waitForValidationMessages() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(validationMessages))
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    private void fillField(By locator, String value) {
        WebElement field = driver.findElement(locator);
        field.clear();
        field.sendKeys(value);
    }
}
