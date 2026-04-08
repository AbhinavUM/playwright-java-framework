package listeners;

import base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import utils.AllureUtil;
import utils.ScreenshotUtil;

import java.nio.file.Path;
import java.util.Optional;

public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("❌ Test Failed: " + context.getDisplayName());

        try {
            BaseTest testInstance = (BaseTest) context.getRequiredTestInstance();

            if (testInstance.page != null && !testInstance.page.isClosed()) {

                // 📸 Screenshot
                ScreenshotUtil.attachScreenshot(
                        testInstance.page,
                        context.getDisplayName()
                );

                // 🎥 Attach Video
                String videoPath = testInstance.page.video().path().toString();
                AllureUtil.attachFile("Video", Path.of(videoPath));

                // 🔍 Attach Trace
                AllureUtil.attachFile("Trace", Path.of("traces/trace.zip"));
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