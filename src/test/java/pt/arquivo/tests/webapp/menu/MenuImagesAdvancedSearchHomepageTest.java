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
public class MenuImagesAdvancedSearchHomepageTest extends MenuTest {

    public MenuImagesAdvancedSearchHomepageTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void menuImagesAdvancedSearchHomepageTest() {
        openMenu();

        run("Open images sub menu", () -> waitUntilElementIsVisibleAndGet(By.id("menu-images")).click());

        run("Click advanced", () -> waitUntilElementIsVisibleAndGet(By.id("menu-images-advanced-search")).click());

        appendError("Check if current url is the advanced image search",
                () -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/image/advanced/search?")));
    }
}
