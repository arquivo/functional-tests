package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;
import pt.fccn.mobile.arquivo.utils.IonicDatePicker;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchLimitedDatesFromHomepageTest extends WebDriverTestBaseParalell {

	public PageSearchLimitedDatesFromHomepageTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void pageSearchLimitedDatesFromHomepageTest() throws Exception {
		run("Search with fccn", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
		});

        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatform().name();
		
		run("Open start date picker", () -> driver.findElement(By.id("date-container-start")).click());

		run("Insert 12/10/1996 on start date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("12/10/1996");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

        run("Open end date picker", () -> driver.findElement(By.id("end-year")).click());

        run("Insert 1/1/1997 on end date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("01/01/1997");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

		run("Click on search button", () -> driver.findElement(By.id("submit-search")).click());
		
		run("Wait until search results are shown", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]")));

		appendError(() -> assertEquals("Verify if the estimated results count message is displayed on page search", "Cerca de 74 resultados desde 1996 até 1997",
				driver.findElement(By.id("estimated-results-value")).getText()));

		appendError("Check first result url", () -> {
			List<WebElement> wes = driver.findElementsByXPath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a");
			assertTrue("Mininium of urls should be 1", wes.size() > 0);

			WebElement we = wes.get(0);
			assertThat("Check first result url", we.getText(), containsString("fccn.pt"));

			assertEquals("After advanced search check search term contains",
				"19961013145650",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getAttribute("data-tstamp").trim());

			assertEquals("After advanced search check search term contains",
				"http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getAttribute("data-url").trim());
		});

		appendError("Check first result title", () -> {
			List<WebElement> wes = driver.findElementsByXPath("//*[@id=\"pages-results\"]/ul[1]/li[1]/a");
			assertTrue("Mininium of title should be 1", wes.size() > 0);

			WebElement we = wes.get(0);
			assertEquals("Check first result title", "www.fccn.pt", we.getText());
		});

		appendError("Check first result version", () -> {
			WebElement we = driver
					.findElementByXPath("//*[@id=\"pages-results\"]/ul[1]/li[3]/p");

			assertThat("Check first result version", we.getText(), containsString("13 Outubro 1996"));
		});

		appendError("Check first result summary", () -> {
			WebElement we = driver
					.findElementByXPath("//*[@id=\"pages-results\"]/ul[1]/li[4]/a/p");

			assertThat("Check first result version", we.getText(), containsString("Av. Brasil"));
		});
	}

}
