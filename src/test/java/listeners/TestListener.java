package listeners;

import base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import utils.AllureUtil;
import utils.ScreenshotUtil;

import java.nio.file.Files;
import java.nio.file.Path;

public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_");

        System.out.println("❌ Test Failed: " + testName);

        try {
            BaseTest testInstance = (BaseTest) context.getRequiredTestInstance();

            if (testInstance.page != null && !testInstance.page.isClosed()) {

                // 📸 Screenshot
                ScreenshotUtil.attachScreenshot(
                        testInstance.page,
                        testName
                );

                // 🎥 Video
                try {
                    if (testInstance.page.video() != null) {
                        Path videoPath = testInstance.page.video().path();

                        if (videoPath != null && Files.exists(videoPath)) {
                            AllureUtil.attachFile("Video", videoPath);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Video attachment failed: " + e.getMessage());
                }

                // 🔍 Trace
                try {
                    Path tracePath = Path.of("traces/" + testName + ".zip");

                    if (Files.exists(tracePath)) {
                        AllureUtil.attachFile("Trace", tracePath);
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ Trace attachment failed: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("⚠️ Attachment failed: " + e.getMessage());
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("✅ Test Passed: " + context.getDisplayName());
    }
}