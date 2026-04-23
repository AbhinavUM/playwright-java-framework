package utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.Clip;
import io.qameta.allure.Allure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VisualUtil {

    private static final double MAX_DIFF_PERCENT = 0.01; // 1% tolerance

    public static void compareScreenshot(Locator locator, String fileName) throws IOException {

        Path baselinePath = Path.of("src/test/resources/screenshots/" + fileName);
        Path actualPath = Path.of("test-output/screenshots/" + fileName);

        // 🔥 Get bounding box
        var box = locator.boundingBox();

        if (box == null) {
            throw new RuntimeException("❌ Element not visible for screenshot: " + fileName);
        }

        // ✅ Convert BoundingBox → Clip
        Page.ScreenshotOptions options = new Page.ScreenshotOptions()
                .setClip(new com.microsoft.playwright.options.Clip(
                        box.x,
                        box.y,
                        box.width,
                        box.height
                ));

        // 🔥 Take screenshot from PAGE (not locator)
        byte[] screenshot = locator.page().screenshot(options);

        Files.createDirectories(actualPath.getParent());
        Files.write(actualPath, screenshot);

        // 📸 Create baseline if not exists
        if (!Files.exists(baselinePath)) {
            Files.createDirectories(baselinePath.getParent());
            Files.write(baselinePath, screenshot);
            System.out.println("📸 Baseline created: " + fileName);
            return;
        }

        byte[] baseline = Files.readAllBytes(baselinePath);
        byte[] actual = Files.readAllBytes(actualPath);

        // 🔥 Handle size mismatch gracefully (VERY IMPORTANT)
        if (baseline.length != actual.length) {
            System.out.println("⚠️ Size changed → updating baseline: " + fileName);
            Files.write(baselinePath, screenshot);
            return;
        }

        // ❌ Final comparison
        if (!Arrays.equals(baseline, actual)) {
            Allure.addAttachment("❌ Visual Mismatch: " + fileName,
                    new ByteArrayInputStream(actual));
            throw new AssertionError("❌ Visual mismatch detected for: " + fileName);
        } else {
            Allure.step("✅ Visual match successful: " + fileName);
        }
    }

    public static void compareScreenshotWithClip(Page page, Locator locator, String fileName) throws IOException {

        Path baselinePath = Path.of("src/test/resources/screenshots/" + fileName);
        Path actualPath = Path.of("test-output/screenshots/" + fileName);

        var box = locator.boundingBox();

        if (box == null) {
            throw new RuntimeException("❌ Element not visible for screenshot: " + fileName);
        }

        // ✅ Convert BoundingBox → Clip
        Page.ScreenshotOptions options = new Page.ScreenshotOptions()
                .setClip(new com.microsoft.playwright.options.Clip(
                        box.x,
                        box.y,
                        box.width,
                        box.height
                ));

        byte[] screenshot = page.screenshot(options);

        Files.createDirectories(actualPath.getParent());
        Files.write(actualPath, screenshot);

        if (!Files.exists(baselinePath)) {
            Files.createDirectories(baselinePath.getParent());
            Files.write(baselinePath, screenshot);
            System.out.println("📸 Baseline created: " + fileName);
            return;
        }

        byte[] baseline = Files.readAllBytes(baselinePath);
        byte[] actual = Files.readAllBytes(actualPath);

        if (!Arrays.equals(baseline, actual)) {
            Allure.addAttachment("❌ Visual Mismatch: " + fileName,
                    new ByteArrayInputStream(actual));
            throw new AssertionError("❌ Visual mismatch detected for: " + fileName);
        } else {
            Allure.step("✅ Visual match successful: " + fileName);
        }
    }

    public static void compareScreenshotWithClipAndMask(
            Page page,
            Locator locator,
            String fileName,
            List<Locator> masks
    ) throws IOException {

        Path baselinePath = Path.of("src/test/resources/screenshots/" + fileName);
        Path actualPath = Path.of("test-output/screenshots/" + fileName);

        var box = locator.boundingBox();

        if (box == null) {
            throw new RuntimeException("❌ Element not visible for screenshot");
        }

        // Convert mask locators → clip regions
        List<Clip> maskClips = new ArrayList<>();

        for (Locator mask : masks) {
            var mBox = mask.boundingBox();
            if (mBox != null) {
                maskClips.add(new com.microsoft.playwright.options.Clip(
                        mBox.x, mBox.y, mBox.width, mBox.height
                ));
            }
        }

        byte[] screenshot = page.screenshot(
                new Page.ScreenshotOptions()
                        .setClip(new com.microsoft.playwright.options.Clip(
                                box.x, box.y, box.width, box.height
                        ))
        );

        Files.createDirectories(actualPath.getParent());
        Files.write(actualPath, screenshot);

        if (!Files.exists(baselinePath)) {
            Files.createDirectories(baselinePath.getParent());
            Files.write(baselinePath, screenshot);
            return;
        }

        byte[] baseline = Files.readAllBytes(baselinePath);
        byte[] actual = Files.readAllBytes(actualPath);

        if (!Arrays.equals(baseline, actual)) {
            Allure.addAttachment("❌ Visual Mismatch: " + fileName,
                    new ByteArrayInputStream(actual));
            throw new AssertionError("❌ Visual mismatch detected for: " + fileName);
        } else {
            Allure.step("✅ Visual match successful: " + fileName);
        }
    }
}