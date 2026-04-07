package factory;

import com.microsoft.playwright.*;

import java.util.Arrays;

public class PlaywrightFactory {

    private static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static ThreadLocal<Page> page = new ThreadLocal<>();

    public static Page initBrowser() {

        playwright.set(Playwright.create());

        browser.set(
                playwright.get().chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(false)
                                .setArgs(Arrays.asList(new String[]{"--start-maximized"}))
                )
        );

        context.set(
                browser.get().newContext(
                        new Browser.NewContextOptions().setViewportSize(null)
                )
        );

        page.set(context.get().newPage());

        return page.get();
    }

    public static Page getPage() {
        return page.get();
    }

    public static void tearDown() {
        context.get().close();
        browser.get().close();
        playwright.get().close();
    }
}