package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import utils.VisualUtil;

import java.util.List;

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
        page.waitForLoadState();
    }

    // 🔥 Login method
    public void login(String baseUrl, String username, String password) {

        if (isHomePageVisible()) {
            System.out.println("Already logged in — skipping login");
            return;
        }

        openLoginPage(baseUrl);

        page.locator(emailInput).fill(username);
        page.locator(passwordInput).fill(password);

        page.locator(passwordInput).press("Tab");
        page.locator(signInBtn).click();

        page.waitForURL("**/home");
    }

    public boolean isHomePageVisible() {
        return page.locator("oh-homepage").isVisible();
    }

    // 🔥 UI + Visual Validation (STABLE VERSION)
    public void verifyLoginPageUI() {

        // 🔥 Disable animations
        page.addStyleTag(new Page.AddStyleTagOptions()
                .setContent("* { transition: none !important; animation: none !important; }"));

        // ✅ Wait for full page readiness
        page.waitForLoadState();

        // 🔥 Wait for critical UI (not just DOM)
        page.waitForSelector("text=Welcome back to OnceHub");
        page.waitForSelector("#email");
        page.waitForSelector("#password");
        page.waitForSelector("#signIn");

        // 🔥 Wait for Google buttons (important async UI)
        page.waitForSelector("text=Sign in with Google");

        // 🔥 Wait for captcha badge to load (and then ignore it)
        page.waitForSelector(".grecaptcha-badge");

        // Small stabilization
        page.waitForTimeout(400);

        // ✅ Functional checks
        PlaywrightAssertions.assertThat(page.getByText("Welcome back to OnceHub")).isVisible();
        PlaywrightAssertions.assertThat(page.getByText("Sign into your account")).isVisible();

        PlaywrightAssertions.assertThat(page.locator("#email")).isVisible();
        PlaywrightAssertions.assertThat(page.locator("#password")).isVisible();
        PlaywrightAssertions.assertThat(page.locator("#signIn")).isVisible();

        // 🔥 Use correct container (NOT form)
        var loginContainer = page.locator(".form-box").first();

        loginContainer.scrollIntoViewIfNeeded();

        // 🔥 Visual validation (correct + stable)
        try {
            VisualUtil.compareScreenshot(
                    loginContainer,
                    "login-page.png"
            );
        } catch (Exception e) {
            throw new RuntimeException("❌ Visual comparison failed", e);
        }
    }
}