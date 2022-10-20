package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.DatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchLimitedDatesFromHomepageTest extends WebDriverTestBaseParallel {

    public PageSearchLimitedDatesFromHomepageTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void pageSearchLimitedDatesFromHomepageTest() throws Exception {
        run("Search with fccn", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
        });

        run("Set start date to 12 October 1996", () -> DatePicker.setStartDatePicker(driver, "12/10/1996"));

        run("Set end date to 1 January 1997", () -> DatePicker.setEndDatePicker(driver, "01/01/1997"));

        run("Click on search button", () -> driver.findElement(By.id("submit-search")).click());

        run("Wait until search results are shown", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]")));

        appendError(() -> assertEquals("Verify if the estimated results count message is displayed on page search", "Cerca de 74 resultados desde 1996 até 1997",
                driver.findElement(By.id("estimated-results-value")).getText()));

        appendError("Check first result url", () -> {
            List<WebElement> wes = driver.findElements(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a"));
            assertTrue("Mininium of urls should be 1", wes.size() > 0);

            WebElement we = wes.get(0);
            assertThat("Check first result url", we.getText(), containsString("fccn.pt"));

            assertEquals("After advanced search check search term contains",
                "19961013145650",
                driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getAttribute("data-tstamp").trim());

            assertEquals("After advanced search check search term contains",
                "http://www.fccn.pt/",
                driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getAttribute("data-url").trim());
        });

        appendError("Check first result title", () -> {
            List<WebElement> wes = driver.findElements(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a"));
            assertTrue("Mininium of title should be 1", wes.size() > 0);

            WebElement we = wes.get(0);
            assertEquals("Check first result title", "www.fccn.pt", we.getText());
        });

        appendError("Check first result version", () -> {
            WebElement we = driver
                    .findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[3]/p"));

            assertThat("Check first result version", we.getText(), containsString("13 Outubro 1996"));
        });

        appendError("Check first result summary", () -> {
            WebElement we = driver
                    .findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[4]/a/p"));

            assertThat("Check first result version", we.getText(), containsString("Av. Brasil"));
        });
    }

}
