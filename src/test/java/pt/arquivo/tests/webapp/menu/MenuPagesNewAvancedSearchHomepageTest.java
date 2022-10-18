package pt.arquivo.tests.webapp.menu;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.mobile.arquivo.tests.menu.MenuTest;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuPagesNewAvancedSearchHomepageTest extends MenuTest {

    public MenuPagesNewAvancedSearchHomepageTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void menuPagesNewAvancedSearchHomepageTest() {
        openMenu();

        run("Open pages sub menu", () -> waitUntilElementIsVisibleAndGet(By.id("menu-pages")).click());

        run("Click new advanced search button",
                () -> driver.findElement(By.id("menu-pages-advanced-search")).click());

        appendError("Check if current url is the advanced search",
                () -> new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains("/page/advanced/search?")));
    }

}
