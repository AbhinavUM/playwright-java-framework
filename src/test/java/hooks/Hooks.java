package hooks;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import io.cucumber.java.*;

import java.nio.file.Files;
import java.nio.file.Path;

public class Hooks {

    public static Page page;

    @Before
    public void setup() {
        page = PlaywrightFactory.initBrowser();
    }

    @After
    public void tearDown(Scenario scenario) {

        String testName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");

        Path videoPath = null;

        try {
            // 📸 Screenshot (only on failure)
            if (scenario.isFailed() && page != null && !page.isClosed()) {
                byte[] screenshot = page.screenshot();
                scenario.attach(screenshot, "image/png", "Screenshot");
            }

            // 🎯 Capture video path before closing
            if (page.video() != null) {
                videoPath = page.video().path();
            }

        } catch (Exception e) {
            System.out.println("⚠️ Pre-teardown error: " + e.getMessage());
        }

        // 🔍 Stop trace + close browser
        PlaywrightFactory.tearDown(testName);

        // ⏳ Wait for files to finalize
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        try {
            Path tracePath = Path.of("traces/" + testName + ".zip");

            if (scenario.isFailed()) {

                // ✅ Attach Video
                if (videoPath != null && Files.exists(videoPath)) {
                    byte[] videoBytes = Files.readAllBytes(videoPath);
                    scenario.attach(videoBytes, "video/webm", "Video");
                }

                // ✅ Attach Trace
                if (Files.exists(tracePath)) {
                    byte[] traceBytes = Files.readAllBytes(tracePath);
                    scenario.attach(traceBytes, "application/zip", "Trace");
                }

            } else {
                // ❌ DELETE artifacts for passed tests

                if (videoPath != null && Files.exists(videoPath)) {
                    Files.delete(videoPath);
                }

                if (Files.exists(tracePath)) {
                    Files.delete(tracePath);
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Post-teardown error: " + e.getMessage());
        }
    }
}