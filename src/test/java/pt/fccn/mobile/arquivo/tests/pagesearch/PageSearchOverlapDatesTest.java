package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.DatePicker;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchOverlapDatesTest extends WebDriverTestBaseParallel {

	public PageSearchOverlapDatesTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void pageSearchOverlapDatesTest() throws Exception {
		run("Search with fccn", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
			driver.findElement(By.id("submit-search")).click();
		});

        run("Set start date to 20 May 1997", () -> DatePicker.setStartDatePicker(driver, "20/05/1997"));

        run("Set end date to 22 August 1996", () -> DatePicker.setEndDatePicker(driver, "22/08/1996"));

		appendError(() -> {
			assertTrue("Check if it is possible to do date overlap: ", checkDatePicker());
		});

	}

	private boolean checkDatePicker() {
		String start = driver.findElement(By.id("start-date")).getAttribute("value");
		String end = driver.findElement(By.id("end-date")).getAttribute("value");
		try{
			return (Integer.parseInt(start) <= Integer.parseInt(end));
		} catch (Error e){
			return false;
		}
	}

}
