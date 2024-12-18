package pt.arquivo.tests.webapp.urlsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.CustomConditions;
import pt.arquivo.utils.LocaleUtils;
import pt.arquivo.utils.LocalizedString;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class URLSearchListNotContainsSomeHttpCodesTest extends WebDriverTestBaseParallel {

    public URLSearchListNotContainsSomeHttpCodesTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void urlSearchListNotContains406PTTest() {
        test("http://www.caleida.pt/saramago/", LocaleUtils.PORTUGUESE, Arrays.asList("list-results-timestamp-19980205082901"),
                Arrays.asList("list-results-timestamp-20120131163447"));
    }

    @Test
    @Retry
    public void urlSearchListNotContains404ENTest() {
        test("http://www.caleida.pt/saramago/", LocaleUtils.ENGLISH, Arrays.asList("list-results-timestamp-20000413142115"),
                Arrays.asList("list-results-timestamp-20160210151550"));
    }

    @Test
    @Retry
    public void urlSearchListNotContains403PTTest() {
        test("sapo.pt", LocaleUtils.PORTUGUESE, Arrays.asList("list-results-timestamp-19971210144509"),
            Arrays.asList("list-results-timestamp-20150424043204"));
    }

    @Test
    @Retry
    public void urlSearchListNotContains503ENTest() {
        test("record.pt", LocaleUtils.ENGLISH, Arrays.asList("list-results-timestamp-19981202152653"),
            Arrays.asList("list-results-timestamp-20171031183600"));
    }

    //Verify if some redirect that is too close of a 200 isn't currently displayed on table/list of versions. Verify the missing of 302 but also verify if the corresponding 200 is displayed.

    @Test
    @Retry
    public void urlSearchListNotContains302PTTest() {
        test("fccn.pt", LocaleUtils.PORTUGUESE, Arrays.asList("list-results-timestamp-20090904173659"),
            Arrays.asList("list-results-timestamp-20161213184117"));
    }

    @Test
    @Retry
    public void urlSearchListNotContains302ENTest() {
        test("fccn.pt", LocaleUtils.ENGLISH, Arrays.asList("list-results-timestamp-20090904173659"),
            Arrays.asList("list-results-timestamp-20161213184117"));
    }

    private void test(String url, Locale locale, List<String> visibleVersions, List<String> invisibleVersions) {

        LocaleUtils.changeLanguageTo(this, locale);

        run("Search url", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(url);
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        LocalizedString listText = new LocalizedString().pt("Lista").en("List");

        assertThat("List button is available",
            waitUntilElementIsVisibleAndGet(By.id("replay-list-button")).getText().trim(), containsString(listText.apply(locale)));

        run("Change to list mode if not in it", () -> {
            WebElement resultsGridCurrentType = waitUntilElementIsVisibleAndGet(By.id("replay-list-button"));
            if (resultsGridCurrentType.getAttribute("disabled") == null) {
                waitUntilElementIsVisibleAndGet(By.id("replay-list-button")).click();
            }
        });

        run("Check specific timestamp should exist", () -> {
            visibleVersions.stream().forEach(version -> {
                WebElement e = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.presenceOfElementLocated(By.id(version)));
                assertNotNull(String.format("The timestamp %s for %s shoud be presented", version, url), e);
            });
        });

        run("Check specific timestamp should not exist", () -> {
            invisibleVersions.stream().forEach(version -> {
                String msg = String.format("The timestamp %s for %s shoud not exist", version, url);

                appendError(msg, () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(CustomConditions.invisibilityOfElementLocatedById(version)));
            });
        });
    }

}
