package factory;

import com.microsoft.playwright.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
                        new Browser.NewContextOptions()
                                .setViewportSize(null)
                                .setRecordVideoDir(Paths.get("videos/")) // 🎥 video
                )
        );

        // 🔍 Start tracing
        context.get().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page.set(context.get().newPage());

        return page.get();
    }

    public static Page getPage() {
        return page.get();
    }

    public static void tearDown(String testName) {

        try {
            // Ensure folder exists
            Files.createDirectories(Paths.get("traces"));

            String safeTestName = testName.replaceAll("[^a-zA-Z0-9]", "_");

            Path tracePath = Paths.get("traces/" + safeTestName + ".zip");

            context.get().tracing().stop(
                    new Tracing.StopOptions().setPath(tracePath)
            );

            System.out.println("📦 Trace saved at: " + tracePath);

        } catch (Exception e) {
            System.out.println("⚠️ Trace stop failed: " + e.getMessage());
        }

        try {
            context.get().close();
            browser.get().close();
            playwright.get().close();
        } catch (Exception e) {
            System.out.println("⚠️ Browser close failed: " + e.getMessage());
        }
    }
}