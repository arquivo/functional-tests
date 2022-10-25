package pt.arquivo.tests.webapp.menu;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuImagesNewSearchHomepageTest extends MenuTest {

    public MenuImagesNewSearchHomepageTest(String os, String version, String browser, String deviceName,
            String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
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
