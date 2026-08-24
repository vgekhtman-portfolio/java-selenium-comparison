package com.vgekhtman.automation.common.api;

import com.vgekhtman.automation.common.config.Config;
import com.vgekhtman.automation.common.model.BookingData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public final class BookingApiClient {

    private static final Logger log = LoggerFactory.getLogger(BookingApiClient.class);

    private static volatile String authToken;

    private BookingApiClient() {
    }

    public static void deleteBooking(BookingData booking) {
        try {
            String token = getAuthToken();
            if (token == null) {
                return;
            }

            Response listing = RestAssured.given()
                    .baseUri(Config.baseUrl())
                    .cookie("token", token)
                    .queryParam("roomid", booking.getRoomType().getRoomId())
                    .get("/api/booking");
            if (listing.getStatusCode() != 200) {
                log.warn("Could not list bookings for room {} - HTTP {}, leaving booking for {} for manual cleanup",
                        booking.getRoomType(), listing.getStatusCode(), booking.getFirstName());
                return;
            }

            String checkIn = booking.getCheckIn().format(DateTimeFormatter.ISO_LOCAL_DATE);
            List<Map> bookings = listing.jsonPath().getList("bookings", Map.class);
            Integer bookingId = bookings.stream()
                    .filter(b -> booking.getFirstName().equals(b.get("firstname")))
                    .filter(b -> checkIn.equals(((Map<?, ?>) b.get("bookingdates")).get("checkin")))
                    .map(b -> (Integer) b.get("bookingid"))
                    .findFirst()
                    .orElse(null);

            if (bookingId == null) {
                log.warn("No matching booking found to delete for {} checking in {}", booking.getFirstName(), booking.getCheckIn());
                return;
            }

            RestAssured.given()
                    .baseUri(Config.baseUrl())
                    .cookie("token", token)
                    .delete("/api/booking/" + bookingId);
        } catch (Exception e) {
            log.warn("Could not delete booking for {} - leaving it for manual cleanup", booking.getFirstName(), e);
        }
    }

    private static synchronized String getAuthToken() {
        if (authToken == null) {
            String requestBody = """
                    {"username":"%s","password":"%s"}"""
                    .formatted(Config.adminUsername(), Config.adminPassword());
            Response response = RestAssured.given()
                    .baseUri(Config.baseUrl())
                    .contentType("application/json")
                    .body(requestBody)
                    .post("/api/auth/login");
            if (response.getStatusCode() != 200) {
                log.warn("Admin login failed with HTTP {} - booking cleanup will be skipped", response.getStatusCode());
                return null;
            }
            authToken = response.jsonPath().getString("token");
        }
        return authToken;
    }
}
