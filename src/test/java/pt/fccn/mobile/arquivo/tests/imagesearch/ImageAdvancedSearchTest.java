package pt.fccn.mobile.arquivo.tests.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 * Test the search of one term in the index interface.
 *
 * @author ivo.branco@fccn.pt
 *
 */
public class ImageAdvancedSearchTest extends WebDriverTestBaseParalell {

    public ImageAdvancedSearchTest(String os, String version, String browser, String deviceName,
            String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void testImageAdvancedSearchPage() throws Exception {

        run("Search FCCN term", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
            driver.findElement(By.id("submit-search")).click();
        });

        run("Search images instead of text", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-form-images")).click();
        });

        run("Click on advanced search link to navigate to advanced search page",
                () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"search-form-advanced\"]/button")).click());

        appendError(() -> {
            assertEquals("Check if search words maintain fccn term", "fccn",
                    driver.findElement(By.xpath("//*[@id=\"advanced-search-form-images\"]/fieldset/input[1]"))
                            .getAttribute("value"));
        });

        run("Open start date picker", () -> driver.findElement(By.id("start-year")).click());

        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatform().name();

        run("Insert 31 may 2010 on start date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("31/05/2010");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

        run("Open end date picker", () -> driver.findElement(By.id("end-year")).click());

        run("Insert 1 jan 2012 on end date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("01/01/2012");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

        appendError("Open select size (images)", () -> driver.findElement(By.id("image-size")).click());

        Select dropdown_size = new Select(driver.findElement(By.id("image-size")));

        appendError("Set size", () -> dropdown_size.selectByValue("sm"));

        appendError("Open format type", () -> driver.findElement(By.id("format-type")).click());

        Select dropdown_type = new Select(driver.findElement(By.id("format-type")));

        appendError("Set format type", () -> dropdown_type.selectByValue("png"));

        appendError("Set site", () -> driver.findElement(By.id("website")).sendKeys("fccn.pt"));

        appendError("Click on search on arquivo.pt button", () -> driver
            .findElement(By.xpath("//*[@id=\"advanced-search-form-images\"]/fieldset/section[2]/button")).click());

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertThat("Check image original origin/domain",
            driver.findElement(By.xpath("//*[@id=\"image-cards-container\"]/li[1]")).getText(),
            containsString("fccn.pt")));

        appendError(() -> assertEquals("Check image date", "20 Janeiro 2011",
            driver.findElement(By.xpath("//*[@id=\"image-cards-container\"]/li[1]/ul/li[5]/p")).getText()));

        appendError(() -> assertEquals("Check image src",
            "https://arquivo.pt/wayback/20110120225358im_/http://fccn.pt/images/announce/modulo_moodle_04109.jpg",
            driver.findElement(By.xpath("//*[@id=\"image-cards-container\"]/li[1]/ul/li[2]/a/img")).getAttribute("src")));

        appendError(() -> assertEquals("After advanced search check search term contains", "fccn site:fccn.pt size:sm type:png safe:on",
            driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        //start date - from
        appendError(() -> assertEquals("After advanced search check day start date contains", "31 Mai",
            driver.findElement(By.id("start-day-month")).getAttribute("value")));

        appendError(() -> assertEquals("After advanced search check year start date contains", "2010",
            driver.findElement(By.id("start-year")).getAttribute("value")));

        // until - end date
        appendError(() -> assertEquals("After advanced search check month end date contains", "1 Jan",
            driver.findElement(By.id("end-day-month")).getAttribute("value")));

        appendError(() -> assertEquals("After advanced search check year end date contains", "2012",
            driver.findElement(By.id("end-year")).getAttribute("value")));
    }
}
