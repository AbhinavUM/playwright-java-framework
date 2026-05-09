package pages.chatonce;

import com.microsoft.playwright.Page;

public class ChatOncePage {

    private Page page;

    public ChatOncePage(Page page) {
        this.page = page;
    }

    // ✅ Verify Bots page (Chatbots header)
    public boolean isBotsPageVisible() {
        return page.getByText("Chatbots").isVisible();
    }
}