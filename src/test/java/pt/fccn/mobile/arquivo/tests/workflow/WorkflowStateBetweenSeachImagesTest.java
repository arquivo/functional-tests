package pt.fccn.mobile.arquivo.tests.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;
import pt.fccn.mobile.arquivo.utils.DatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class WorkflowStateBetweenSeachImagesTest extends WebDriverTestBaseParalell {

    public WorkflowStateBetweenSeachImagesTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void stateBetweenSeachImagesTest() throws Exception {
        run("Search FCCN term", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
            driver.findElement(By.id("submit-search")).click();
        });

        run("Click Image Button", () -> {
            driver.findElement(By.id("search-form-images")).click();
        });

        run("Set start date to 20 June 1997", () -> DatePicker.setStartDatePicker(driver, "20/06/1997"));

        run("Set end date to 1 January 2014", () -> DatePicker.setEndDatePicker(driver, "01/01/2014"));

        run("Click on search button", () -> driver.findElement(By.id("submit-search")).click());

        run("Go to the next page", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"images-results\"]/section/form/button")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("images-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on second page", "fccn",
                driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1997",
                driver.findElement(By.id("start-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "20 Jun",
                driver.findElement(By.id("start-day-month")).getAttribute("value").trim()));


        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "2014",
                driver.findElement(By.id("end-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1 Jan",
                driver.findElement(By.id("end-day-month")).getAttribute("value").trim()));

        run("Go to the previous page", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"images-results\"]/section/form[1]/button")).click();
        });

        appendError(() -> assertEquals("Check if fccn is in search box on second page", "fccn",
                driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1997",
                driver.findElement(By.id("start-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "20 Jun",
                driver.findElement(By.id("start-day-month")).getAttribute("value").trim()));


        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "2014",
                driver.findElement(By.id("end-year")).getAttribute("value").trim()));

        appendError(() -> assertEquals("Check if 1977 is in the year left datepicker", "1 Jan",
                driver.findElement(By.id("end-day-month")).getAttribute("value").trim()));

    }

}
