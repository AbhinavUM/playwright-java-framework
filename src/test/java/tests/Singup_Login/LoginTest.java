package tests.Singup_Login;

import base.BaseTest;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.Test;
import pages.Homepage.Homepage;
import pages.Signup_Login.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    private final String baseUrl = "https://account2.onceplatform.com/";

    @Test
    void user_can_login_with_valid_credentials_and_land_on_homepage() {

        LoginPage loginPage = new LoginPage(page);
        Homepage homepage = new Homepage(page);

        performLoginFlow(loginPage);

        verifyUserOnHomePage(loginPage);

        verifyHomepageUI(homepage);

        attachFinalScreenshot();
    }

    // =========================
    // 🔹 REUSABLE FLOW
    // =========================

    private void performLoginFlow(LoginPage loginPage) {

        Allure.step("Open login page", () ->
                loginPage.openLoginPage(baseUrl)
        );

        Allure.step("Verify login page UI and layout visually", loginPage::verifyLoginPageUI
        );

        Allure.step("Login with user: " + "abhinav4376@gmail.com", () ->
                loginPage.login(baseUrl, "abhinav4376@gmail.com", "Qwerty@123")
        );
    }

    private void verifyUserOnHomePage(LoginPage loginPage) {

        Allure.step("Verify user lands on home page", () ->
                assertTrue(
                        loginPage.isHomePageVisible(),
                        "❌ Home page not visible after login"
                )
        );
    }

    // =========================
    // 🔥 HOMEPAGE VALIDATION
    // =========================

    private void verifyHomepageUI(Homepage homepage) {

        Allure.step("Verify top navigation (functional)",
                homepage::verifyTopNavigation
        );

        Allure.step("Verify left navigation (functional)",
                homepage::verifyLeftNavigation
        );

        Allure.step("Verify sidebar visually (stable UI)",
                homepage::verifySidebarVisually
        );

        Allure.step("Verify top navigation visually (masked dynamic UI)",
                homepage::verifyTopNavVisually
        );
    }

    private void attachFinalScreenshot() {

        Allure.step("Attach final full page screenshot (debug purpose only)", () ->
                Allure.addAttachment(
                        "Final UI",
                        new java.io.ByteArrayInputStream(
                                page.screenshot(
                                        new Page.ScreenshotOptions().setFullPage(true)
                                )
                        )
                )
        );
    }
}