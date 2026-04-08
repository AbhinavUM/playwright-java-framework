package stepdefs;

import hooks.Hooks;
import io.cucumber.java.en.*;
import pages.LoginPage;
import utils.ConfigReader;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginSteps {

    LoginPage loginPage;

    @Given("I am on login page")
    public void openLoginPage() {
        loginPage = new LoginPage(Hooks.page);
        loginPage.openLoginPage(ConfigReader.get("base.url"));
    }

    @When("I login with email {string} and password {string}")
    public void login(String email, String password) {
        loginPage.smartLogin(
                ConfigReader.get("base.url"),
                email,
                password
        );
    }

    @Then("I should land on home page")
    public void verifyHomePage() {
        assertTrue(
                loginPage.isHomePageVisible(),
                "❌ Home page not visible after login"
        );
    }
}