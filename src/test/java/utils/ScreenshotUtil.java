package utils;

import com.microsoft.playwright.Page;

import java.nio.file.Paths;

public class ScreenshotUtil {

    public static void takeScreenshot(Page page, String testName) {
        page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshots/" + testName + ".png"))
                .setFullPage(true));
    }
}