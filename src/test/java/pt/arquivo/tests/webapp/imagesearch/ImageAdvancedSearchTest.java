package pt.arquivo.tests.webapp.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

/**
 * Test the search of one term in the index interface.
 *
 * @author ivo.branco@fccn.pt
 *
 */
public class ImageAdvancedSearchTest extends WebDriverTestBaseParallel {

    public ImageAdvancedSearchTest(Map<String, String> config) {
        super(config);
    }

    private void iosCompatibleWaitUntilVisibleAndClick(String cssSelector){
            Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
            String platform = capabilities.getPlatformName().name();
            if (platform.toLowerCase().equals("ios")){
                // IOS driver is dumb and sometimes fails to click properly. So instead we use javascript to click.
                waitUntilElementIsVisibleAndGet(By.cssSelector(cssSelector));
                ((JavascriptExecutor) driver).executeScript("document.querySelector('"+cssSelector+"').click()");
                
            } else {
                waitUntilElementIsVisibleAndGet(By.cssSelector(cssSelector)).click();
            }
    }

    private void iosCompatibleWaitUntilVisibleAndSelect(String cssSelector, String value){
        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatformName().name();
        if (platform.toLowerCase().equals("ios")){
            // IOS driver is dumb again.
            waitUntilElementIsVisibleAndGet(By.cssSelector(cssSelector));
            ((JavascriptExecutor) driver).executeScript("var ele = document.querySelector('"+cssSelector+"'); for(var i=0; i<ele.options.length;i++) {if (ele.options[i].value === '"+value+"') { ele.options[i].selected = true; break;}}");
            
        } else {
            waitUntilElementIsVisibleAndGet(By.cssSelector(cssSelector)).click();
            Select dropdown_size = new Select(waitUntilElementIsVisibleAndGet(By.cssSelector(cssSelector)));
            dropdown_size.selectByValue(value);

        }
    }


    @Test
    @Retry
    public void imageAdvancedSearchPageTest() throws Exception {

        run("Search FCCN term", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Search images instead of text", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-form-images")).click();
        });

        run("Click on advanced search link to navigate to advanced search page",
                () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"search-form-advanced\"]/button")).click());

        appendError(() -> {
            assertEquals("Check if search words maintain fccn term", "fccn",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"advanced-search-form-images\"]/fieldset/input[1]"))
                            .getAttribute("value"));
        });

        run("Set start date to 31 may 2010", () -> DatePicker.setStartDatePicker(driver, "31/05/2010"));

        run("Set end date to 1 jan 2012", () -> DatePicker.setEndDatePicker(driver, "01/01/2012"));

        appendError("Select size to small images", () -> iosCompatibleWaitUntilVisibleAndSelect("#image-size","sm"));

        appendError("Unselect 'All formats'", () -> iosCompatibleWaitUntilVisibleAndClick("input[type=checkbox][format=all]"));

        appendError("Set format type to 'PNG'", () -> iosCompatibleWaitUntilVisibleAndClick("input[type=checkbox][format=png]"));

        appendError("Set site", () -> waitUntilElementIsVisibleAndGet(By.id("website")).sendKeys("fccn.pt"));

        appendError("Click on search on arquivo.pt button", () -> driver
            .findElement(By.xpath("//*[@id=\"advanced-search-form-images\"]/fieldset/section[2]/button")).click());

        System.out.println("Current url: " + driver.getCurrentUrl());

        appendError(() -> assertThat("Check image original origin/domain",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"image-cards-container\"]/li[1]")).getText().trim(),
            containsString("fccn.pt")));

        appendError(() -> assertEquals("Check image date", "20 Janeiro 2011",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"image-cards-container\"]/li[1]/ul/li[5]/p")).getText().trim()));

        appendError(() -> assertEquals("Check image src",
            "https://arquivo.pt/wayback/20110120225358im_/http://fccn.pt/images/announce/modulo_moodle_04109.jpg",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"image-cards-container\"]/li[1]/ul/li[2]/a/img")).getAttribute("src")));

        appendError(() -> assertEquals("After advanced search check search term contains", "fccn site:fccn.pt size:sm type:png",
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));

        System.out.println("Current url: " + driver.getCurrentUrl());

        //start date - from
        appendError(() -> assertEquals("After advanced search check day start date contains", "31 Mai",
            waitUntilElementIsVisibleAndGet(By.id("start-day-month")).getAttribute("value")));

        appendError(() -> assertEquals("After advanced search check year start date contains", "2010",
            waitUntilElementIsVisibleAndGet(By.id("start-year")).getAttribute("value")));

        // until - end date
        appendError(() -> assertEquals("After advanced search check month end date contains", "1 Jan",
            waitUntilElementIsVisibleAndGet(By.id("end-day-month")).getAttribute("value")));

        appendError(() -> assertEquals("After advanced search check year end date contains", "2012",
            waitUntilElementIsVisibleAndGet(By.id("end-year")).getAttribute("value")));
    }
}
