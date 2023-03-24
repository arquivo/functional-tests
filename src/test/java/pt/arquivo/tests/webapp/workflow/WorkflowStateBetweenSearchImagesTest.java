package pt.arquivo.tests.webapp.workflow;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class WorkflowStateBetweenSearchImagesTest extends WebDriverTestBaseParallel {

    public WorkflowStateBetweenSearchImagesTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void stateBetweenSearchImagesTest() throws Exception {
        run("Search FCCN term", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Click Image Button", () -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.id("search-form-images"))).click());

        run("Set start date to 20 June 1997", () -> DatePicker.setStartDatePicker(driver, "20/06/1997"));

        run("Set end date to 1 January 2014", () -> DatePicker.setEndDatePicker(driver, "01/01/2014"));

        run("Click on search button", () -> waitUntilElementIsVisibleAndGet(By.id("submit-search")).click());

        run("Go to the next page", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"images-results\"]/section/form/button")).click();
        });

        waitUntilElementIsVisibleAndGet(By.id("images-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on second page", "fccn",
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
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"images-results\"]/section/form[1]/button")).click();
        });

        appendError(() -> assertEquals("Check if fccn is in search box on second page", "fccn",
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

    }

}
