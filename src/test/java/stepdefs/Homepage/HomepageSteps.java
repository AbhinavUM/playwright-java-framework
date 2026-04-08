package stepdefs.Homepage;

import hooks.Hooks;
import io.cucumber.java.en.*;
import pages.Homepage.Homepage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomepageSteps {

    Homepage homepage;

    @Then("I should see Bots icon on the left outer navigation")
    public void verifyBotsIconVisible() {
        homepage = new Homepage(Hooks.page);

        assertTrue(
                homepage.isBotsIconVisible(),
                "❌ Bots icon is not visible on homepage"
        );
    }

    @When("I click on Bots icon")
    public void clickBotsIcon() {
        homepage = new Homepage(Hooks.page);
        homepage.clickBotsIcon();
    }
}