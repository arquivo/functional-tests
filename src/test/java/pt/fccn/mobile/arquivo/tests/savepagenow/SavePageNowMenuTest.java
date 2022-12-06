package pt.fccn.mobile.arquivo.tests.savepagenow;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class SavePageNowMenuTest extends WebDriverTestBaseParallel {

    public SavePageNowMenuTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
    }

    @Test
    @Retry
    public void savePageNowMenuTest() {
        savePageNowMenu("ddadfcfe.cdsffds", "http://info.cern.ch/");
    }

    private void savePageNowMenu(String wrongQuery, String goodQuery) {

        run("Open left menu", () -> {
            waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click();
            waitUntilElementIsVisibleAndGet(By.id("left-nav"));
        });

        run("Open Save Page Now", () -> {
            waitUntilElementIsVisibleAndGet(By.id("menu-savepagenow")).click();
        });

        run("Check Save Page Now", () -> {
            waitUntilElementIsVisibleAndGet(By.id("logo-save-page-now"));
        });

        appendError(() -> assertEquals("Verify slogan from Save Page Now", "Grava páginas no Arquivo.pt",
            driver.findElement(By.id("save-page-now-slogan")).getText()));

        run("Search wrong query", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys(wrongQuery);
            driver.findElement(By.id("submit-search")).click();
        });

        appendError(() -> assertEquals("Verify error from Save Page Now", "Endereço não encontrado. Verifique se o endereço está correto.",
            driver.findElement(By.id("save-page-url-message-error")).getText()));

        run("Search query", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys(goodQuery);
            driver.findElement(By.id("submit-search")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("logo-arquivo"));

        driver.switchTo().frame("replay_iframe");

        assertThat("Verify if the \"http://info.cern.ch - home of the first website\" is displayed on the CERN web page",
            driver.findElement(By.xpath("/html/body/h1")).getText().toLowerCase(), containsString("http://info.cern.ch - home of the first website"));

        driver.switchTo().defaultContent();

        run("Conclude Save Page Now", () -> {
            waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click();
        });

        assertThat("Verify text from modal",
            driver.findElement(By.xpath("//*[@id=\"savepagenow-modal\"]/p")).getText().toLowerCase(), containsString("a sua navegação foi salva e ficará disponível em breve no arquivo.pt"));

        run("Click ok", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"savepagenow-modal\"]/form/button"));
        });

        run("Check Save Page Now Homepage", () -> {
            waitUntilElementIsVisibleAndGet(By.id("logo-save-page-now"));
        });
    }
}
