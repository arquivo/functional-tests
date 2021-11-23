package pt.fccn.mobile.arquivo.tests.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.RemoteWebDriver;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class WorkflowStateBetweenSearchPageAndImageTest extends WebDriverTestBaseParalell {

    public WorkflowStateBetweenSearchPageAndImageTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void stateBetweenSearchPageAndImageTest() throws Exception {
        run("Search FCCN term", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
            driver.findElement(By.id("submit-search")).click();
        });

        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatform().name();

        run("Open start date picker", () -> driver.findElement(By.id("date-container-start")).click());

        run("Insert 20/06/1997 on start date picker", () -> {
            if (platform.equals("LINUX") || platform.equals("WINDOWS") || platform.equals("ANY")) {
                driver.findElementById("modal-datepicker-input").sendKeys("20/06/1997");
            } else {
                System.out.println("TODO: Android test");
            }
        });

        run("Click OK", () -> {
            if (platform.equals("LINUX") || platform.equals("WINDOWS")) {
                driver.findElementById("modal-datepicker-confirm-button").click();
            } else {
                driver.findElementById("modal-datepicker-confirm-button").sendKeys(Keys.ENTER);
            }
        });

        run("Open end date picker", () -> driver.findElement(By.id("end-year")).click());

        run("Insert 1/1/2014 on end date picker", () -> {
            if (platform.equals("LINUX") || platform.equals("WINDOWS") || platform.equals("ANY")) {
                driver.findElementById("modal-datepicker-input").sendKeys("01/01/2014");
            } else {
                System.out.println("TODO: Android test");
            }
        });

        run("Click OK", () -> {
            if (platform.equals("LINUX") || platform.equals("WINDOWS")) {
                driver.findElementById("modal-datepicker-confirm-button").click();
            } else {
                driver.findElementById("modal-datepicker-confirm-button").sendKeys(Keys.ENTER);
            }
        });

        run("Click on Image search button", () -> driver.findElement(By.id("search-form-images")).click());

        waitUntilElementIsVisibleAndGet(By.id("images-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on image search", "fccn",
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

        run("Click on Page search button", () -> driver.findElement(By.id("search-form-pages")).click());

        waitUntilElementIsVisibleAndGet(By.id("pages-results"));

        appendError(() -> assertEquals("Check if fccn is in search box on image search", "fccn",
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
