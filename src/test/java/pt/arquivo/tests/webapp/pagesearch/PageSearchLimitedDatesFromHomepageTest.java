package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
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
            waitUntilElementIsVisibleAndGet(By.id("estimated-results-value")).getText().trim(),
            CoreMatchers.allOf(
                containsString("Cerca de "),
                containsString(" resultados desde 1996 até 1997")
            )
        ));

        appendError("Check first result url", () -> {
            List<WebElement> wes = driver.findElements(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a"));
            assertTrue("Mininium of urls should be 1", wes.size() > 0);

            WebElement we = wes.get(0);
            assertThat("Check first result url", we.getText().trim(), containsString("fccn.pt"));

            assertEquals("After advanced search check search term contains",
                "19961013145650",
                waitUntilElementIsVisibleAndGet(By.cssSelector("#pages-results > ul:first-of-type")).getAttribute("data-tstamp").trim());

            assertThat("After advanced search check search term contains fccn.pt",
                waitUntilElementIsVisibleAndGet(By.cssSelector("#pages-results > ul:first-of-type")).getAttribute("data-url").trim(),
                containsString("fccn.pt")
            );
        });

        appendError("Check first result title", () -> {
            List<WebElement> wes = driver.findElements(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a"));
            assertTrue("Mininium of title should be 1", wes.size() > 0);

            WebElement we = wes.get(0);
            assertThat("Check first result title", 
                we.getText().trim(),
                containsString("fccn.pt"));
        });

        appendError("Check first result version", () -> {
            WebElement we = driver
                    .findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[3]/p"));

            assertThat("Check first result version", we.getText().trim(), containsString("13 Outubro 1996"));
        });

        appendError("Check first result summary", () -> {
            WebElement we = driver
                    .findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[4]/p"));

            assertThat("Check first result version", we.getText().trim(), containsString("Av. Brasil"));
        });
    }

}
