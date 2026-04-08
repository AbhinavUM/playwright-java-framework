package pages;

import com.microsoft.playwright.Page;

public class LoginPage {

    private Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    private String emailInput = "#email";
    private String passwordInput = "#password";
    private String signInBtn = "#signIn";

    public void openLoginPage(String baseUrl) {
        page.navigate(baseUrl + "signin");
    }

    // 🔥 SMART LOGIN
    public void smartLogin(String baseUrl, String username, String password) {

        // ✅ Step 1: Check if already logged in
        if (page.locator("oh-homepage").isVisible()) {
            System.out.println("Already logged in — skipping login");
            return;
        }

        // ✅ Step 2: Open login page
        openLoginPage(baseUrl);

        page.locator(emailInput).waitFor();

        // ✅ Step 3: Fill credentials
        page.locator(emailInput).fill(username);
        page.locator(passwordInput).fill(password);

        page.locator(passwordInput).press("Tab");

        // ✅ Step 4: Click login
        page.locator(signInBtn).click();

        // ✅ Step 5: Wait for home page
        page.waitForURL("**/home");
    }

    public boolean isHomePageVisible() {
        return page.locator("oh-homepage").count() > 0;
    }
}