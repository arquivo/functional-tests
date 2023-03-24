package pt.arquivo.tests.webapp.menu;

import java.time.Duration;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuImagesNewSearchHomepageTest extends MenuTest {

    public MenuImagesNewSearchHomepageTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void menuImagesNewSearchHomepageTest() {
        openMenu();

        run("Open images sub menu", () -> waitUntilElementIsVisibleAndGet(By.id("menu-images")).click());

        run("Click new search button",
                () -> waitUntilElementIsVisibleAndGet(By.id("menu-images-new-search")).click());

        appendError("Check if current url is the image search",
                () -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/image/search?")));
    }
}
