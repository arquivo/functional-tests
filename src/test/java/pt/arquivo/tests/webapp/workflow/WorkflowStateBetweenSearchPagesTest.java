package pt.arquivo.tests.webapp.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class WorkflowStateBetweenSearchPagesTest extends WebDriverTestBaseParallel {

    public WorkflowStateBetweenSearchPagesTest(String os, String version, String browser, String deviceName,
            String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
    }


    @Test
    @Retry
    public void stateBetweenSearchPages() throws Exception {
        run("Search FCCN term", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
        });

        run("Set start date to 20 June 1997", () -> DatePicker.setStartDatePicker(driver, "20/06/1997"));

        run("Set end date to 1 January 2014", () -> DatePicker.setEndDatePicker(driver, "01/01/2014"));

        run("Click Search Button", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("pages-results"));

        run("Go to the next page", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/section/form/button")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("pages-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on image search", "fccn",
                waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1997",
                waitUntilElementIsVisibleAndGet(By.id("start-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "20 Jun",
                waitUntilElementIsVisibleAndGet(By.id("start-day-month")).getAttribute("value").trim()));


        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "2014",
                waitUntilElementIsVisibleAndGet(By.id("end-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1 Jan",
                waitUntilElementIsVisibleAndGet(By.id("end-day-month")).getAttribute("value").trim()));

        run("Go to the previous page", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/section/form[1]/button")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("pages-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on image search", "fccn",
                waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1997",
                waitUntilElementIsVisibleAndGet(By.id("start-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "20 Jun",
                waitUntilElementIsVisibleAndGet(By.id("start-day-month")).getAttribute("value").trim()));


        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "2014",
                waitUntilElementIsVisibleAndGet(By.id("end-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1 Jan",
                waitUntilElementIsVisibleAndGet(By.id("end-day-month")).getAttribute("value").trim()));

        appendError("Check if fccn is in search box on second page",
                () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@value=\"fccn\"]")));

    }

}
