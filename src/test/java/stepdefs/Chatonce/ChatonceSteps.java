package stepdefs.Chatonce;

import hooks.Hooks;
import io.cucumber.java.en.Then;
import pages.Chatonce.Chatoncepage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChatonceSteps {

    Chatoncepage chatoncePage;

    @Then("I should see the Bots page")
    public void verifyBotsPage() {
        chatoncePage = new Chatoncepage(Hooks.page);

        assertTrue(
                chatoncePage.isBotsPageVisible(),
                "❌ Bots page is not displayed"
        );
    }
}