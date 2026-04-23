package base;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BaseTest {

    public Page page;
    private String testName;

    @BeforeEach
    void setup(TestInfo testInfo) {

        page = PlaywrightFactory.initBrowser();

        // 🔥 DO NOT set viewport here (handled in factory via fullscreen)
        // page.setViewportSize(1280, 800); ❌ REMOVE

        // 🔥 Stabilize UI rendering (safe)
        if (page != null) {
            page.evaluate("document.body.style.zoom = '1'");
        }

        testName = testInfo.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");
    }

    @AfterEach
    void tearDown() {

        Path videoPath = null;

        try {
            // 📸 Screenshot (always attach)
            if (page != null && !page.isClosed()) {
                byte[] screenshot = page.screenshot();
                Allure.addAttachment("Screenshot", new ByteArrayInputStream(screenshot));
            }

            // 🎥 Capture video BEFORE closing
            if (page != null && page.video() != null) {
                videoPath = page.video().path();
            }

        } catch (Exception e) {
            System.out.println("⚠️ Pre-teardown error: " + e.getMessage());
        }

        // 🔍 Stop tracing + close browser
        try {
            if (testName != null) {
                PlaywrightFactory.tearDown(testName);
            }
        } catch (Exception e) {
            System.out.println("⚠️ TearDown error: " + e.getMessage());
        }

        // ⏳ Wait for files to flush
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {}

        try {
            if (testName != null) {

                Path tracePath = Path.of("traces/" + testName + ".zip");

                // 🎥 Attach video
                if (videoPath != null && Files.exists(videoPath)) {
                    byte[] videoBytes = Files.readAllBytes(videoPath);
                    Allure.addAttachment(
                            "Video",
                            "video/webm",
                            new ByteArrayInputStream(videoBytes),
                            ".webm"
                    );
                }

                // 📦 Attach trace
                if (Files.exists(tracePath)) {
                    byte[] traceBytes = Files.readAllBytes(tracePath);
                    Allure.addAttachment(
                            "Trace",
                            "application/zip",
                            new ByteArrayInputStream(traceBytes),
                            ".zip"
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Post-teardown error: " + e.getMessage());
        }
    }
}