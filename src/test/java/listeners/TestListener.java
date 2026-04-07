package listeners;

import base.BaseTest;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import utils.ScreenshotUtil;

import java.util.Optional;

public class TestListener implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("❌ Test Failed: " + context.getDisplayName());

        try {
            BaseTest testInstance = (BaseTest) context.getRequiredTestInstance();

            if (testInstance.page != null && !testInstance.page.isClosed()) {
                ScreenshotUtil.takeScreenshot(
                        testInstance.page,
                        context.getDisplayName().replaceAll("[^a-zA-Z0-9]", "_")
                );
            }
        } catch (Exception e) {
            System.out.println("⚠️ Screenshot failed: " + e.getMessage());
        }
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("✅ Test Passed: " + context.getDisplayName());
    }
}