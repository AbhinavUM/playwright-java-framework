package tests.bookingcalendar;

import base.BaseTest;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import pages.bookingcalendar.BookingCalendarPage;
import pages.homepage.HomePage;
import pages.signuplogin.LoginPage;
import utils.BookingCalendarCreationType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingCalendarBookingFlowTest extends BaseTest {

    @Test
    void user_can_create_booking_calendar_and_complete_booking() {

        LoginPage loginPage = new LoginPage(page);
        HomePage homePage = new HomePage(page);
        BookingCalendarPage bookingCalendarPage = new BookingCalendarPage(page);

        String baseUrl = "https://account2.onceplatform.com/";

        String email = "abhinav4376@gmail.com";
        String password = "Qwerty@123";

        String bookingCalendarName = "PW-Auto-BKC";

        // =========================
        // LOGIN
        // =========================

        Allure.step("Login into application", () ->
                loginPage.login(baseUrl, email, password)
        );

        // =========================
        // HOMEPAGE VALIDATION
        // =========================

        Allure.step("Verify homepage loaded", () -> {

            homePage.verifyTopNavigation();

            homePage.verifyLeftNavigation();
        });

        // =========================
        // OPEN BOOKING CALENDARS
        // =========================

        Allure.step("Open Booking Calendars module", bookingCalendarPage::openBookingCalendars
        );

        // =========================
        // VERIFY PAGE
        // =========================

        Allure.step("Verify Booking Calendar page loaded", bookingCalendarPage::verifyBookingCalendarPageLoaded
        );

        // =========================
        // CREATE BOOKING CALENDAR
        // FROM SCRATCH FLOW
        // =========================

        bookingCalendarPage.createBookingCalendar(
                "PW-Auto-BKC",
                "Playwright Meeting",
                BookingCalendarCreationType.FROM_SCRATCH,
                null
        );

        // =========================
        // CREATE BOOKING CALENDAR
        // FROM TEMPLATE FLOW
        // =========================

//        bookingCalendarPage.createBookingCalendar(
//                "PW-Auto-BKC",
//                "Playwright Meeting",
//                BookingCalendarCreationType.FROM_TEMPLATE,
//                "System template workflow"
//        );

        // =========================
        // CONFIGURE HOST & SAVE
        // =========================

        Allure.step("Configure host and save Booking Calendar settings", () ->
                bookingCalendarPage.configureHostAndSave("Abhinav")
        );

        // =========================
        // OPEN CUSTOMER FRONT
        // =========================

        Allure.step("Open customer booking page", bookingCalendarPage::openCustomerFront
        );

        // =========================
        // COMPLETE BOOKING
        // =========================

        Allure.step("Complete booking from customer front", () ->
                bookingCalendarPage.completeBooking(
                        "Abhinav",
                        "abhinav4376@gmail.com"
                ));

        // =========================
        // VERIFY BOOKING
        // =========================

        Allure.step("Verify booking confirmation", () ->

                bookingCalendarPage.verifyBookingConfirmed(
                        "Playwright Meeting",
                        "Abhinav",
                        "abhinav4376@gmail.com"
                )
        );


        // =========================
        // FINAL SCREENSHOT
        // =========================

        Allure.step("Attach final screenshot", () ->
                Allure.addAttachment(
                        "Final UI",
                        new java.io.ByteArrayInputStream(
                                page.screenshot(
                                        new Page.ScreenshotOptions()
                                                .setFullPage(true)
                                )
                        )
                )
        );

        // =========================
        // SWITCH BACK TO ADMIN TAB
        // =========================

        Allure.step("Switch back to admin tab", bookingCalendarPage::switchBackToAdminTab
        );

        // =========================
        // DELETE BOOKING CALENDAR
        // =========================

        Allure.step("Delete Booking Calendar", () ->

                bookingCalendarPage.deleteBookingCalendar(
                        bookingCalendarName
                )
        );
    }
}