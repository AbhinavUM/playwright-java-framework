//package pages.homepage;
//
//import com.microsoft.playwright.Page;
//import com.microsoft.playwright.assertions.PlaywrightAssertions;
//import com.microsoft.playwright.options.AriaRole;
//import utils.VisualUtil;
//
//public class HomePage {
//
//    private final Page page;
//
//    public HomePage(Page page) {
//        this.page = page;
//    }
//
//    // =========================
//    // 🔹 TOP NAV VALIDATION
//    // =========================
//    public void verifyTopNavigation() {
//
//        // Wait for top nav
//        page.waitForSelector("#Mobileheader");
//
//        // Stable waits
//        page.waitForSelector("text=Scheduled Meetings");
//        page.waitForSelector("text=Live Engagements");
//        page.waitForSelector("#notificationIcon");
//
//        // Functional validations
//        PlaywrightAssertions.assertThat(
//                page.getByRole(
//                        AriaRole.LINK,
//                        new Page.GetByRoleOptions()
//                                .setName("Scheduled Meetings")
//                )
//        ).isVisible();
//
//        PlaywrightAssertions.assertThat(
//                page.getByText("Live Engagements").first()
//        ).isVisible();
//
//        PlaywrightAssertions.assertThat(
//                page.locator("#notificationIcon")
//        ).isVisible();
//
//        PlaywrightAssertions.assertThat(
//                page.getByTitle("Help and support").first()
//        ).isVisible();
//
//        // Strict-safe locator
//        PlaywrightAssertions.assertThat(
//                page.locator("button[title='Account']").first()
//        ).isVisible();
//    }
//
//    // =========================
//    // 🔹 TOP NAV VISUAL
//    // =========================
//    public void verifyTopNavVisually() {
//
//        // Wait for top nav container
//        page.waitForSelector("#Mobileheader");
//
//        // Wait for critical labels/icons
//        page.waitForSelector("text=Scheduled Meetings");
//        page.waitForSelector("text=Live Engagements");
//
//        page.waitForSelector("#notificationIcon");
//        page.waitForSelector("[title='Help and support']");
//        page.waitForSelector("button[title='Account']");
//
//        // Wait for images/icons rendering
//        page.waitForFunction("""
//            () => {
//                const icons = document.querySelectorAll("#Mobileheader img");
//                return icons.length >= 1 &&
//                       [...icons].every(img => img.complete);
//            }
//        """);
//
//        var topNav = page.locator("#Mobileheader");
//
//        topNav.scrollIntoViewIfNeeded();
//
//        // Stabilization
//        page.waitForTimeout(500);
//
//        try {
//            VisualUtil.compareScreenshot(
//                    topNav,
//                    "topnav.png"
//            );
//        } catch (Exception e) {
//            throw new RuntimeException(
//                    "❌ Top nav visual validation failed",
//                    e
//            );
//        }
//    }
//
//    // =========================
//    // 🔹 LEFT NAV VALIDATION
//    // =========================
//    public void verifyLeftNavigation() {
//
//        page.waitForSelector("sl-outer-sidenav-bar");
//
//        String[] navItems = {
//                "Home",
//                "Booking Calendars",
//                "Booking Hubs",
//                "Booking Pages",
//                "Routing Forms",
//                "Chatbots",
//                "AI Agents",
//                "Activities",
//                "Contacts",
//                "Analytics"
//        };
//
//        for (String item : navItems) {
//
//            page.waitForSelector("text=" + item);
//
//            PlaywrightAssertions.assertThat(
//                    page.getByText(item).first()
//            ).isVisible();
//        }
//    }
//
//    // =========================
//    // 🔹 SIDEBAR VISUAL
//    // =========================
//    public void verifySidebarVisually() {
//
//        // Wait for sidebar container
//        page.waitForSelector("sl-outer-sidenav-bar");
//
//        // Wait for critical nav items
//        page.waitForSelector("text=Booking Calendars");
//        page.waitForSelector("text=Chatbots");
//        page.waitForSelector("text=Analytics");
//
//        // Wait for icons to render
//        page.waitForSelector("oui-icon[svgicon='chatbots_big']");
//        page.waitForSelector("oui-icon[svgicon='booking-calendars']");
//        page.waitForSelector("oui-icon[svgicon='nav-analytics']");
//
//        // Wait for SVG rendering
//        page.waitForFunction("""
//            () => {
//                const icons = document.querySelectorAll(
//                    "sl-outer-sidenav-bar svg"
//                );
//
//                return icons.length >= 5;
//            }
//        """);
//
//        var sidebar = page.locator("sl-outer-sidenav-bar");
//
//        sidebar.scrollIntoViewIfNeeded();
//
//        // Final stabilization
//        page.waitForTimeout(800);
//
//        try {
//            VisualUtil.compareScreenshot(
//                    sidebar,
//                    "sidebar.png"
//            );
//        } catch (Exception e) {
//            throw new RuntimeException(
//                    "❌ Sidebar visual validation failed",
//                    e
//            );
//        }
//    }
//
//    // =========================
//    // 🔹 FULL HOMEPAGE VALIDATION
//    // =========================
//    public void verifyHomepageLoaded() {
//
//        verifyTopNavigation();
//
//        verifyLeftNavigation();
//
//        verifyTopNavVisually();
//
//        verifySidebarVisually();
//    }
//}








package pages.homepage;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import utils.VisualUtil;

public class HomePage {

    private final Page page;

    public HomePage(Page page) {
        this.page = page;
    }

    // =========================
    // 🔹 TOP NAV VALIDATION
    // =========================
    public void verifyTopNavigation() {

        PlaywrightAssertions.assertThat(
                page.getByRole(AriaRole.LINK,
                        new Page.GetByRoleOptions().setName("Scheduled Meetings"))
        ).isVisible();

        PlaywrightAssertions.assertThat(
                page.getByText("Live Engagements").first()
        ).isVisible();

        PlaywrightAssertions.assertThat(
                page.locator("#notificationIcon")
        ).isVisible();

        PlaywrightAssertions.assertThat(
                page.getByTitle("Help and support").first()
        ).isVisible();

        // ✅ Strict-safe locator
        PlaywrightAssertions.assertThat(
                page.locator("button[title='Account']").first()
        ).isVisible();
    }

    public void verifyTopNavVisually() {

        // ✅ Wait for top nav container
        page.waitForSelector("#Mobileheader");

        // ✅ Wait for ALL critical elements (icons + labels)
        page.waitForSelector("text=Scheduled Meetings");
        page.waitForSelector("text=Live Engagements");

        page.waitForSelector("#notificationIcon");
        page.waitForSelector("[title='Help and support']");
        page.waitForSelector("button[title='Account']");

        // ✅ Wait for SVG icons (VERY IMPORTANT)
        page.waitForFunction("""
        () => {
            const icons = document.querySelectorAll("#Mobileheader img");
            return icons.length >= 3 && [...icons].every(img => img.complete);
        }
    """);

        var topNav = page.locator("#Mobileheader");

        topNav.scrollIntoViewIfNeeded();

        // 🔥 Final stabilization (render + fonts)
        page.waitForTimeout(300);

        try {
            VisualUtil.compareScreenshot(
                    topNav,
                    "topnav.png"
            );
        } catch (Exception e) {
            throw new RuntimeException("❌ Top nav visual validation failed", e);
        }
    }

    // =========================
    // 🔹 LEFT NAV VALIDATION
    // =========================
    public void verifyLeftNavigation() {

        String[] navItems = {
                "Home",
                "Booking Calendars",
                "Booking Hubs",
                "Booking Pages",
                "Routing Forms",
                "Chatbots",
                "AI Agents",
                "Activities",
                "Contacts",
                "Analytics"
        };

        for (String item : navItems) {
            PlaywrightAssertions.assertThat(
                    page.getByText(item).first()
            ).isVisible();
        }
    }

    // =========================
    // 🔹 VISUAL (STABLE + CORRECT)
    // =========================
    public void verifySidebarVisually() {

        // Step 1: wait for sidebar container
        page.waitForSelector("sl-outer-sidenav-bar");

        // 🔥 Step 2: wait for icons (critical fix)
        page.waitForSelector("oui-icon[svgicon='chatbots_big']");

        // Optional: ensure one more element
        page.waitForSelector("text=Booking Calendars");

        var sidebar = page.locator("sl-outer-sidenav-bar");

        // Step 3: ensure it's in viewport
        sidebar.scrollIntoViewIfNeeded();

        // 🔥 Step 4: extra stabilization (very important)
        page.waitForTimeout(800);

        try {
            VisualUtil.compareScreenshot(
                    sidebar,
                    "sidebar.png"
            );
        } catch (Exception e) {
            throw new RuntimeException("❌ Sidebar visual validation failed", e);
        }
    }
}