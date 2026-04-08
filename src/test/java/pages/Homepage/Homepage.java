package pages.Homepage;

import com.microsoft.playwright.Page;

public class Homepage {

    private Page page;

    public Homepage(Page page) {
        this.page = page;
    }

    public boolean isBotsIconVisible() {
        return page.getByLabel("Chatbots_big").isVisible();
    }

    public void clickBotsIcon() {
        page.getByLabel("Chatbots_big").click();
    }
}