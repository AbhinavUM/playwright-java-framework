package factory;

import com.microsoft.playwright.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    private static final ThreadLocal<Browser> browser = new ThreadLocal<>();
    private static final ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    private static final ThreadLocal<Page> page = new ThreadLocal<>();

    public static Page initBrowser() {

        playwright.set(Playwright.create());

        browser.set(
                playwright.get().chromium().launch(
                        new BrowserType.LaunchOptions()
                                .setHeadless(false)
                                // 🔥 REAL fullscreen window (not just viewport)
                                .setArgs(Arrays.asList("--start-maximized"))
                )
        );

        context.set(
                browser.get().newContext(
                        new Browser.NewContextOptions()
                                // 🔥 CRITICAL: disable fixed viewport → use window size
                                .setViewportSize(null)
                                // 🎥 Record video
                                .setRecordVideoDir(Paths.get("videos/"))
                )
        );

        // 🔍 Start tracing (screenshots + DOM snapshots)
        context.get().tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page.set(context.get().newPage());

        // 🔥 Stabilize rendering early (reduces visual flakiness)
        page.get().addInitScript(
                "(() => { document.body && (document.body.style.zoom = '1'); })();"
        );

        return page.get();
    }

    public static Page getPage() {
        return page.get();
    }

    public static void tearDown(String testName) {

        try {
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
            // Close in correct order
            if (context.get() != null) context.get().close();
            if (browser.get() != null) browser.get().close();
            if (playwright.get() != null) playwright.get().close();

        } catch (Exception e) {
            System.out.println("⚠️ Browser close failed: " + e.getMessage());
        }
    }
}