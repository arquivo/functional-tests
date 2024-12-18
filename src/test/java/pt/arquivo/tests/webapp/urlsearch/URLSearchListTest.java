package pt.arquivo.tests.webapp.urlsearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.CustomConditions;
import pt.arquivo.utils.DateUtils;
import pt.arquivo.utils.LocaleUtils;
import pt.arquivo.utils.LocalizedString;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class URLSearchListTest extends WebDriverTestBaseParallel {

    public URLSearchListTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void urlSearchListPTTest() {
        urlSearchListTest("fccn.pt", LocaleUtils.PORTUGUESE);
    }

    @Test
    @Retry
    public void urlSearchListENTest() {
        urlSearchListTest("fccn.pt", LocaleUtils.ENGLISH);
    }

    private void urlSearchListTest(String url, Locale locale) {
        LocaleUtils.changeLanguageTo(this, locale);

        run("Search fccn.pt url", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(url);
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        String versionLabel = new LocalizedString().pt("versão").en("version").apply(locale);

        System.out.println("Current url: " + driver.getCurrentUrl());

        run("Verify year", () -> {
            WebElement yearTableHeader = waitUntilElementIsVisibleAndGet(By.id("list-results-year-1996"));
            assertNotNull("Verify if year table header exist", yearTableHeader);

            appendError("Year 1995 shouldn't be visible", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(CustomConditions.invisibilityOfElementLocatedById("list-results-year-1995")));

            WebElement yearWebElement = yearTableHeader.findElement(By.xpath("//*[@id=\"list-results-year-1996\"]/a"));
            assertNotNull("Year web element not found", yearWebElement);
            assertThat("Year is available", yearWebElement.getText().trim(), containsString("1996"));

            assertThat("Verify versions",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"list-results-year-1996\"]/a/span")).getText().trim(), containsString("1 " + versionLabel));

            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"list-results-year-1996\"]/a")).click();
        });

        run("Verify month", () -> {
            String monthContent = waitUntilElementIsVisibleAndGet(By.id("list-results-month-1996-10")).getText().trim();
            String monthVersionLocal = new LocalizedString().pt("1 versão").en("1 version").apply(locale);
            String monthLocal = new LocalizedString().pt("Outubro").en("October").apply(locale);
            assertThat("Verify month version", monthContent, containsString(monthVersionLocal));
            assertThat("Verify versions", monthContent, containsString(monthLocal));
        });

        appendError("September shouldn't be visible", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(CustomConditions.invisibilityOfElementLocatedById("list-results-month-1996-09")));
        appendError("November shouldn't be visible", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(CustomConditions.invisibilityOfElementLocatedById("list-results-month-1996-11")));

        run("Open october", () -> {
            WebElement monthHeaderWE = waitUntilElementIsVisibleAndGet(By.id("list-results-month-1996-10"));
            assertNotNull("Month should be not null", monthHeaderWE);
            monthHeaderWE.click();
        });

        appendError("Verify specific timestamp", () -> {
            System.out.println("Current url: " + driver.getCurrentUrl());
            String timestamp = "19961013145650";
            WebElement dayWE = waitUntilElementIsVisibleAndGet(By.id("list-results-timestamp-" + timestamp));
            //System.out.println("List results value: " + dayWE.getText().trim());

            //System.out.println("Locale value: " + locale.toString());
            MessageFormat messageFormat = new MessageFormat("{0,date,d} {0,date,MMMM} {0,time,HH}h{0,time,mm}, {0,time,yyyy}", locale);
            //System.out.println("Message format: " + messageFormat.toPattern());
            Object[] dates = {DateUtils.asDateFromTimestamp(timestamp)};
            /*for (Object date: dates) {
                System.out.println(date.toString());
            }*/
            //System.out.println("Message format locate: " + messageFormat.getLocale());
            String expected = messageFormat.format(dates, new StringBuffer(), null).toString();
            //System.out.println("Expected list results value: " + expected);

            assertEquals(expected, dayWE.getText().trim());

            assertThat("Verify href",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"list-results-timestamp-19961013145650\"]/a")).getAttribute("href").replace(":80", ""), containsString("/wayback/19961013145650/http://www.fccn.pt/"));
        });
    }

    public static void main(String[] args) {
        MessageFormat messageFormat = new MessageFormat("{0,date,d} {0,date,MMMM} {0,time,HH}h{0,time,mm}, {0,time,yyyy}");
        System.out.println(messageFormat.toPattern());
        System.out.println(DateUtils.asDateFromTimestamp("19961013145650"));
        Object[] dates = {DateUtils.asDateFromTimestamp("19961013145650")};
        String expected = messageFormat.format(dates, new StringBuffer(), null).toString();
        System.out.println(expected);
    }
}