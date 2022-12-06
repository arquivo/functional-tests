package pt.fccn.mobile.arquivo.tests.urlsearch;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.DateUtils;
import pt.fccn.mobile.arquivo.utils.LocaleUtils;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class URLSearchTableTest extends WebDriverTestBaseParallel {

    public URLSearchTableTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
    }

    @Test
    @Retry
    public void urlSearchTableTestPT() {
        LocaleUtils.changeLanguageToPT(this);
        urlSearchTableTest("fccn.pt", "Tabela", "13 Out", LocaleUtils.PORTUGUESE);
    }

    @Test
    @Retry
    public void urlSearchTableTestEN() {
        LocaleUtils.changeLanguageToEN(this);
        urlSearchTableTest("fccn.pt", "Table", "13 Oct", LocaleUtils.ENGLISH);
    }

    private void urlSearchTableTest(String url, String tableText, String firstResultText, Locale locale) {

        run("Search fccn.pt url", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys(url);
            driver.findElement(By.id("submit-search")).click();
        });

        run("Change to Table mode if not in it", () -> {
            WebElement resultsGridCurrentType = driver.findElement(By.id("replay-table-button"));
            if (resultsGridCurrentType.getAttribute("disabled") == null) {
                driver.findElement(By.id("replay-table-button")).click();
            }
        });

        System.out.println("Current url: " + driver.getCurrentUrl());

        run("Check if first version match", () -> {
            waitUntilElementIsVisibleAndGet(By.id("url-results"));
            assertEquals("Check if first version match", firstResultText,
                driver.findElement(By.id("table-cell-19961013145650")).getText());
        });

        appendError("Verify specific timetamp", () -> {
            String timestamp = "table-cell-19961013145650";
            WebElement dayWE = waitUntilElementIsVisibleAndGet(By.id(timestamp));
            assertEquals(firstResultText, dayWE.getText());
        });
    }
}
