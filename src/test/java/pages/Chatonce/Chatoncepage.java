package pages.Chatonce;

import com.microsoft.playwright.Page;

public class Chatoncepage {

    private Page page;

    public Chatoncepage(Page page) {
        this.page = page;
    }

    // ✅ Verify Bots page (Chatbots header)
    public boolean isBotsPageVisible() {
        return page.getByText("Chatbots").isVisible();
    }
}