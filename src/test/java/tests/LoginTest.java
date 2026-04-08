package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest extends BaseTest {

    @Test
    void verifyLoginFlow() {

        LoginPage loginPage = new LoginPage(page);

        loginPage.smartLogin(
                ConfigReader.get("base.url"),
                "abhinav4376@gmail.com",
                "Qwerty@123"
        );

        // Assertion
        assertTrue(page.locator("oh-homehvpage").isVisible());
    }
}