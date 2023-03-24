package pt.arquivo.tests.webapp.urlsearch;

import static org.junit.Assert.assertEquals;

import java.util.Locale;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class URLSearchTableTest extends WebDriverTestBaseParallel {

    public URLSearchTableTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void urlSearchTablePTTest() {
        LocaleUtils.changeLanguageToPT(this);
        urlSearchTableTest("fccn.pt", "Tabela", "13 Out", LocaleUtils.PORTUGUESE);
    }

    @Test
    @Retry
    public void urlSearchTableENTest() {
        LocaleUtils.changeLanguageToEN(this);
        urlSearchTableTest("fccn.pt", "Table", "13 Oct", LocaleUtils.ENGLISH);
    }

    private void urlSearchTableTest(String url, String tableText, String firstResultText, Locale locale) {

        run("Search fccn.pt url", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(url);
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Change to Table mode if not in it", () -> {
            WebElement resultsGridCurrentType = waitUntilElementIsVisibleAndGet(By.id("replay-table-button"));
            if (resultsGridCurrentType.getAttribute("disabled") == null) {
                waitUntilElementIsVisibleAndGet(By.id("replay-table-button")).click();
            }
        });

        System.out.println("Current url: " + driver.getCurrentUrl());

        run("Check if first version match", () -> {
            waitUntilElementIsVisibleAndGet(By.id("url-results"));
            assertEquals("Check if first version match", firstResultText,
                waitUntilElementIsVisibleAndGet(By.id("table-cell-19961013145650")).getText().trim());
        });

        appendError("Verify specific timetamp", () -> {
            String timestamp = "table-cell-19961013145650";
            WebElement dayWE = waitUntilElementIsVisibleAndGet(By.id(timestamp));
            assertEquals(firstResultText, dayWE.getText().trim());
        });
    }
}
