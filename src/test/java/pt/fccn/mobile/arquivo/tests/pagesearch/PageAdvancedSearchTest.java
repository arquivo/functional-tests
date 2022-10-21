package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageAdvancedSearchTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}
	
	@Test
	@Retry
	public void testPageAdvancedSearch() throws Exception {
		run("Search with fccn", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
			driver.findElement(By.id("submit-search")).click();
		});
		
		run("Click on advanced search link to navigate to advanced search page",
				() -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"search-form-advanced\"]/button")).click());
		
		appendError(() -> {
			assertEquals("Check if search words maintain fccn term", "fccn",
					driver.findElement(By.id("words")).getAttribute("value"));
		});
		

		//run("Open start date picker", () -> driver.findElement(By.id("date-container-start")).click());

		//run("Insert 31 may 2010 on start date picker", () -> {
		//	IonicDatePicker.changeTo(driver, LocalDate.of(2006, 5, 31));
		//});
		
		//run("Open end date picker", () -> driver.findElement(By.id("date-container-end")).click());
		//run("Insert 1 jan 2019 on end date picker", () -> {
		//	IonicDatePicker.changeTo(driver, LocalDate.of(2019, 1, 1));
		//});
		
        appendError("Open format type", () -> driver.findElement(By.id("format-type")).click());
		
		Select dropdown_type = new Select(driver.findElement(By.id("format-type")));
		
		appendError("Set format type",
				() -> dropdown_type.selectByValue("pdf"));

		appendError("Set site", () -> driver.findElement(By.id("website")).sendKeys("fccn.pt"));
		
		appendError("Click on search on arquivo.pt button", () -> driver.findElement(By.xpath("//*[@id=\"advanced-search-form-pages\"]/fieldset/section[2]/button")).click());
		
		appendError(() -> assertEquals("After advanced search check search term contains",
				"fccn site:fccn.pt type:pdf",
				driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));

		System.out.println("Current url: " + driver.getCurrentUrl());
		
		appendError(() -> assertEquals("Check mime of first result", "[PDF]",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[2]/a/span")).getText()));
		
		appendError(() -> assertEquals("Check url of first result", "fccn.pt/wp-content/uploads/2017/06/booklet_RCTS2017.pdf",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul/li[1]/a")).getText()));
		
		// start date - from
		//appendError(() -> assertEquals("After advanced search check day start date contains", "31",
		//		driver.findElement(By.id("calendarDayStart")).getText()));

		//appendError(() -> assertEquals("After advanced search check month start date contains", "Mai",
		//		driver.findElement(By.id("calendarMonthStart")).getText()));

		//appendError(() -> assertEquals("After advanced search check year start date contains", "2006",
		//		driver.findElement(By.id("calendarYearStart")).getText()));

		// until - end date
		//appendError(() -> assertEquals("After advanced search check day end date contains", "1",
		//		driver.findElement(By.id("calendarDayEnd")).getText()));

		//appendError(() -> assertEquals("After advanced search check month end date contains", "Jan",
		//		driver.findElement(By.id("calendarMonthEnd")).getText()));

		//appendError(() -> assertEquals("After advanced search check year end date contains", "2019",
		//		driver.findElement(By.id("calendarYearEnd")).getText()));
	}

}
