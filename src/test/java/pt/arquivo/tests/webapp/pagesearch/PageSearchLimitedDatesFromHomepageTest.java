package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchLimitedDatesFromHomepageTest extends WebDriverTestBaseParallel {

    public PageSearchLimitedDatesFromHomepageTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void pageSearchLimitedDatesFromHomepageTest() throws Exception {
        run("Search with fccn", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
        });

        run("Set start date to 12 October 1996", () -> DatePicker.setStartDatePicker(driver, "12/10/1996"));

        run("Set end date to 1 January 1997", () -> DatePicker.setEndDatePicker(driver, "01/01/1997"));

        run("Click on search button", () -> waitUntilElementIsVisibleAndGet(By.id("submit-search")).click());

        run("Wait until search results are shown", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]")));

        appendError(() -> assertThat("Verify if the estimated results count message is displayed on page search", 
            waitUntilElementIsVisibleAndGet(By.id("estimated-results")).getText().trim(),
            CoreMatchers.allOf(
                containsString("Cerca de "),
                containsString(" resultados desde 1996 até 1997")
            )
        ));

        appendError("Check results are within the selected dates", () -> {
            List<WebElement> results = driver.findElements(By.cssSelector("#pages-results > ul"));
            assertTrue("Mininium of results should be 1", results.size() > 0);
            for (WebElement result : results) {
                String tstamp = result.getAttribute("data-tstamp").trim();
                assertTrue("Result " + tstamp + " should be between 12 October 1996 and 1 January 1997",
                    tstamp.compareTo("19961012") >= 0 && tstamp.compareTo("19970102") < 0);
            }
        });

        appendError("Check first result", () -> {
            WebElement first = waitUntilElementIsVisibleAndGet(By.cssSelector("#pages-results > ul:first-of-type"));

            assertThat("Check first result url", first.getAttribute("data-url").trim(), containsString("fccn.pt"));
            assertThat("Check first result shown url",
                first.findElement(By.cssSelector(".results-url")).getText().trim(), containsString("fccn.pt"));
            assertFalse("Check first result title isn't empty",
                first.findElement(By.cssSelector(".results-title")).getText().trim().isEmpty());
        });

        // The ranking differs between environments, so look for the fccn.pt home page anywhere on the results
        By homepageResultBy = By.cssSelector("#pages-results > ul[data-tstamp=\"19961013145650\"]");

        appendError("Check fccn.pt home page result version", () -> {
            assertThat("Check fccn.pt home page result version",
                driver.findElement(homepageResultBy).findElement(By.cssSelector(".results-date")).getText().trim(),
                containsString("13 Outubro 1996"));
        });

        appendError("Check fccn.pt home page result summary", () -> {
            assertThat("Check fccn.pt home page result summary",
                driver.findElement(homepageResultBy).findElement(By.cssSelector(".results-summary")).getText().trim(),
                containsString("Av. Brasil"));
        });
    }

}
