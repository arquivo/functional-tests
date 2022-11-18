package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.DatePicker;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchOverlapDatesTest extends WebDriverTestBaseParallel {

	public PageSearchOverlapDatesTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
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
		String start = waitUntilElementIsVisibleAndGet(By.id("start-date")).getAttribute("value");
		String end = waitUntilElementIsVisibleAndGet(By.id("end-date")).getAttribute("value");
		try{
			return (Integer.parseInt(start) <= Integer.parseInt(end));
		} catch (Error e){
			return false;
		}
	}

}
