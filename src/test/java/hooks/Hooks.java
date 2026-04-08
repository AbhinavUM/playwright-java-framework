package hooks;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks {

    public static Page page;

    @Before
    public void setup() {
        page = PlaywrightFactory.initBrowser();
    }

    @After
    public void tearDown(io.cucumber.java.Scenario scenario) {

        String testName = scenario.getName();

        if (scenario.isFailed()) {
            System.out.println("❌ Test Failed: " + testName);
        }

        PlaywrightFactory.tearDown(testName);
    }
}