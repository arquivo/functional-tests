package pt.fccn.mobile.arquivo.tests.menu;

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
public class MenuPagesNewSearchHomepageTest extends MenuTest {

    public MenuPagesNewSearchHomepageTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void menuPagesNewSearchHomepageTest() {
        openMenu();

        run("Open pages sub menu", () -> waitUntilElementIsVisibleAndGet(By.id("menu-pages")).click());

        run("Click new search button",
                () -> waitUntilElementIsVisibleAndGet(By.id("menu-pages-new-search")).click());

        appendError("Check if current url is the page search",
                () -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/page/search?")));
    }

}
