package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.DatePicker;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchOverlapDatesTest extends WebDriverTestBaseParallel {

	public PageSearchOverlapDatesTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	@Test
	@Retry
	public void pageSearchOverlapDatesTest() throws Exception {
		run("Search with fccn", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

        run("Set start date to 20 May 1997", () -> DatePicker.setStartDatePicker(driver, "20/05/1997"));

        run("Set end date to 22 August 1996", () -> DatePicker.setEndDatePicker(driver, "22/08/1996"));

		appendError(() -> {
			assertTrue("Check if it is possible to do date overlap: ", checkDatePicker());
		});

	}

	private boolean checkDatePicker() {
		LocalDate start = DatePicker.getStartDate(driver);
		LocalDate end = DatePicker.getEndDate(driver);
		return start.isBefore(end) || start.isEqual(end);
	}

}
