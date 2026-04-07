package base;

import com.microsoft.playwright.Page;
import factory.PlaywrightFactory;
import listeners.TestListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TestListener.class)
public class BaseTest {

    public Page page;

    @BeforeEach
    void setup() {
        page = PlaywrightFactory.initBrowser();
    }

    @AfterEach
    void tearDown() {
        PlaywrightFactory.tearDown();
    }
}