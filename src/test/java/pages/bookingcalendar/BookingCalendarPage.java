package pages.bookingcalendar;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import utils.BookingCalendarCreationType;

public class BookingCalendarPage {

    private Page page;
    private Page adminPage;

    public BookingCalendarPage(Page page) {
        this.page = page;
    }

    // =========================
    // OPEN BOOKING CALENDARS
    // =========================
    public void openBookingCalendars() {

        // Wait for sidebar
        page.waitForSelector("sl-outer-sidenav-bar");

        // Click Booking Calendars
        page.locator("a[href*='scheduling/calendars']").click();

        // Wait for navigation
        page.waitForURL(url -> url.contains("scheduling/calendars"));

        // Page stabilization
        page.waitForLoadState();
        page.waitForTimeout(1500);

        // ✅ URL verification
        String currentUrl = page.url();

        if (!currentUrl.contains("scheduling/calendars")) {
            throw new RuntimeException(
                    "❌ Failed to navigate to Booking Calendars page. Current URL: "
                            + currentUrl
            );
        }

        // ✅ UI verification
        PlaywrightAssertions.assertThat(
                page.getByText("Booking Calendars Lobby")
        ).isVisible();

        PlaywrightAssertions.assertThat(
                page.getByRole(
                        AriaRole.BUTTON,
                        new Page.GetByRoleOptions()
                                .setName("Create booking calendar")
                )
        ).isVisible();

        // ✅ Table verification
        PlaywrightAssertions.assertThat(
                page.locator("table")
        ).isVisible();

        System.out.println("✅ Successfully landed on Booking Calendars page");
    }

    // =========================
    // CREATE BOOKING CALENDAR
    // =========================
    public void createBookingCalendar(
            String bookingCalendarName,
            String meetingSubject,
            BookingCalendarCreationType creationType,
            String templateName
    ) {

        // =========================
        // OPEN CREATE POPUP
        // =========================

        page.waitForURL(url -> url.contains("scheduling/calendars"));

        page.locator("button:has-text('Create booking calendar')")
                .click();

        page.waitForSelector("#bookingCalendarCreatePopup");

        // =========================
        // CREATION TYPE
        // =========================

        if (creationType == BookingCalendarCreationType.FROM_TEMPLATE) {

            page.locator("#fromTemplate")
                    .click();

            page.waitForSelector("oui-select");

            page.locator("oui-select")
                    .click();

            page.waitForSelector(".oui-select-panel");

            page.locator(".oui-option-text")
                    .filter(
                            new com.microsoft.playwright.Locator.FilterOptions()
                                    .setHasText(templateName)
                    )
                    .first()
                    .click();

            System.out.println(
                    "✅ Template selected: " + templateName
            );

        } else {

            page.locator("#fromScratch")
                    .click();

            System.out.println(
                    "✅ Creating Booking Calendar from scratch"
            );
        }

        // =========================
        // FORM FIELDS
        // =========================

        page.locator("#bkcName")
                .fill(bookingCalendarName);

        page.locator("#bkcMeetingSubject")
                .fill(meetingSubject);

        // =========================
        // CREATE
        // =========================

        page.locator(
                "#bookingCalendarCreatePopup button:has-text('Create')"
        ).click();

        page.waitForLoadState();

        page.waitForTimeout(2000);

        PlaywrightAssertions.assertThat(
                page.getByText(bookingCalendarName).first()
        ).isVisible();

        System.out.println(
                "✅ Booking Calendar created successfully"
        );
    }

    // =========================
    // VERIFY BOOKING CALENDAR
    // =========================
    public boolean isBookingCalendarVisible(String bookingCalendarName) {

        return page.getByText(bookingCalendarName)
                .first()
                .isVisible();
    }

    // =========================
    // CONFIGURE HOST & SAVE
    // =========================
    public void configureHostAndSave(String hostName) {

        // =========================
        // VERIFY SETTINGS PAGE
        // =========================

        page.waitForLoadState();

        PlaywrightAssertions.assertThat(
                page.locator("label[for='attendees']")
        ).isVisible();

        // =========================
        // OPEN HOST DROPDOWN
        // =========================

        page.locator("#attendees")
                .click();

        // Wait dropdown
        page.waitForSelector(".oui-select-options");

        // =========================
        // SEARCH HOST
        // =========================

        page.locator("input[aria-label='Type to filter']")
                .fill(hostName);

        // Wait search result
        page.waitForTimeout(1000);

        // =========================
        // SELECT USER
        // =========================

        page.locator("oui-option")
                .filter(
                        new com.microsoft.playwright.Locator.FilterOptions()
                                .setHasText(hostName)
                )
                .first()
                .click();

        // =========================
        // CLICK ADD
        // =========================

        page.locator("button:has-text('Add')")
                .click();

        // =========================
        // VERIFY HOST TILE
        // =========================

        page.waitForSelector(".selected-members");

        PlaywrightAssertions.assertThat(
                page.locator(
                        ".selected-members div[title='" + hostName + " ']"
                )
        ).isVisible();

        System.out.println(
                "✅ Host added successfully: " + hostName
        );

        // =========================
        // CLICK SAVE
        // =========================

        page.locator("#interactionSaveButton")
                .click();

        // =========================
        // WAIT FOR SAVE
        // =========================

        page.waitForLoadState();

        page.waitForTimeout(3000);

        // =========================
        // VERIFY SAVE SUCCESS
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator("#interactionSaveButton")
        ).isDisabled();

        System.out.println(
                "✅ Booking Calendar settings saved successfully"
        );
    }

    // =========================
        // OPEN CUSTOMER FRONT
    // =========================
    public void openCustomerFront() {

        // =========================
        // SAVE ADMIN TAB
        // =========================

        this.adminPage = this.page;

        // =========================
        // OPEN PAGE DESIGNER
        // =========================

        page.waitForLoadState();

        page.waitForSelector("text=Page Designer");

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions().setName("Page Designer")
        ).click();

        // =========================
        // VERIFY PAGE DESIGNER PAGE
        // =========================

        page.waitForLoadState();

        page.waitForSelector(".page-url-box-in");

        PlaywrightAssertions.assertThat(
                page.locator(".page-url-box-in")
        ).isVisible();

        // =========================
        // OPEN CUSTOMER FRONT IN NEW TAB
        // =========================

        Page customerFrontPage = page.context().waitForPage(() -> {

            page.locator(".page-url-box-in")
                    .click();
        });

        // =========================
        // WAIT FOR CUSTOMER FRONT
        // =========================

        customerFrontPage.waitForLoadState();

        customerFrontPage.waitForSelector(
                "#bookingCalendarContainer"
        );

        // =========================
        // SWITCH CONTROL TO NEW TAB
        // =========================

        this.page = customerFrontPage;

        // =========================
        // VERIFY CUSTOMER FRONT
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator("#bookingCalendarContainer")
        ).isVisible();

        System.out.println("✅ Customer front opened successfully");
    }

    // =========================
    // COMPLETE BOOKING
    // =========================
    public void completeBooking(
            String name,
            String email
    ) {

        // =========================
        // WAIT FOR BOOKING PAGE
        // =========================

        page.waitForSelector("#bookingCalendarContainer");

        // =========================
        // SELECT NEXT AVAILABLE DATE
        // =========================

        Locator availableDates = page.locator(
                "button[data-testid='calendar-date-button']:not([disabled])"
        );

        // Skip currently selected date and choose next one
        availableDates.nth(1).click();

        // =========================
        // WAIT FOR TIMESLOTS
        // =========================

        page.waitForSelector("#timeSlotsList");

        // =========================
        // SELECT FIRST AVAILABLE TIMESLOT
        // =========================

        page.locator("button[data-testid='handleSelectDate-button']")
                .first()
                .click();

        // =========================
        // CLICK CONFIRM BUTTON
        // =========================

        page.locator("button[data-testid='Confirm-Button']")
                .first()
                .click();

        // =========================
        // WAIT FOR BOOKING FORM
        // =========================

        page.waitForSelector("#scheduleForm");

        // =========================
        // FILL FULL NAME
        // =========================

        page.locator("textarea[name='name']")
                .fill(name);

        // =========================
        // FILL EMAIL
        // =========================

        page.locator("input[name='email']")
                .fill(email);

        // =========================
        // CLICK SCHEDULE
        // =========================

        page.locator("button[data-testid='submit-button']")
                .click();

        // =========================
        // WAIT FOR CONFIRMATION
        // =========================

        page.waitForLoadState();

        System.out.println("✅ Booking completed successfully");
    }

    // =========================
    // VERIFY BOOKING CONFIRMATION
    // =========================
    public void verifyBookingConfirmed(
            String meetingSubject,
            String hostName,
            String email
    ) {

        // =========================
        // WAIT FOR CONFIRMATION PAGE
        // =========================

        page.waitForLoadState();

        // Wait for visible meeting subject
        page.waitForSelector("h2");

        // =========================
        // VERIFY MEETING SUBJECT
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator("h2")
                        .filter(
                                new Locator.FilterOptions()
                                        .setHasText(meetingSubject)
                        )
                        .first()
        ).isVisible();

        // =========================
        // VERIFY HOST
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator("#meeting-host")
                        .last()
        ).containsText(hostName);

        // =========================
        // VERIFY EMAIL
        // =========================

        PlaywrightAssertions.assertThat(
                page.getByText(email)
                        .last()
        ).isVisible();

        System.out.println("✅ Booking confirmation verified successfully");
    }

    // =========================
    // VISUAL VALIDATION
    // =========================
    public void verifyBookingCalendarPageLoaded() {

        PlaywrightAssertions.assertThat(
                page.getByText("Booking Calendars").first()
        ).isVisible();
    }

    // =========================
    // SWITCH BACK TO ADMIN TAB
    // =========================
    public void switchBackToAdminTab() {

        // Close customer front tab
        page.close();

        // Switch back to original admin tab
        this.page = adminPage;

        page.bringToFront();

        page.waitForLoadState();

        System.out.println("✅ Switched back to admin tab");
    }

    // =========================
    // DELETE BOOKING CALENDAR
    // =========================
    public void deleteBookingCalendar(String bookingCalendarName) {

        // =========================
        // GO BACK TO BKC LOBBY
        // =========================

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("Booking calendars lobby")
        ).click();

        page.waitForLoadState();

        // =========================
        // SEARCH BOOKING CALENDAR
        // =========================

        page.locator("input[placeholder='Search']")
                .fill(bookingCalendarName);

        page.waitForTimeout(1000);

        // =========================
        // VERIFY BKC APPEARS
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator(".booking-calendar-name")
                        .getByText(bookingCalendarName)
        ).isVisible();

        // =========================
        // OPEN THREE DOT MENU
        // =========================

        Locator bookingCalendarRow = page.locator("tr")
                .filter(
                        new Locator.FilterOptions()
                                .setHasText(bookingCalendarName)
                )
                .first();

        bookingCalendarRow.locator("#settings-button")
                .click();

        // =========================
        // CLICK DELETE
        // =========================

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.MENUITEM,
                new Page.GetByRoleOptions()
                        .setName("Delete")
        ).click();

        // =========================
        // VERIFY DELETE POPUP
        // =========================

        PlaywrightAssertions.assertThat(
                page.locator("label.oui-dialog-header-title")
        ).containsText("Delete Booking Calendar");

        // =========================
        // CLICK DELETE BUTTON
        // =========================

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Delete booking calendar")
        ).click();
        // =========================
        // VERIFY SUCCESS POPUP
        // =========================

        PlaywrightAssertions.assertThat(
                page.getByText("Booking Calendar Deleted")
        ).isVisible();

        // =========================
        // CLOSE SUCCESS POPUP
        // =========================

        page.getByRole(
                com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Close")
        ).click();

        // =========================
        // VERIFY BKC REMOVED
        // =========================

        PlaywrightAssertions.assertThat(
                page.getByText("No results matching your filter criteria")
        ).isVisible();

        System.out.println("✅ Booking Calendar deleted successfully");
    }
}