package pt.arquivo.tests.webapp.pagesearch;

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

public class PageAdvancedSearchTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchTest(Map<String, String> config) {
		super(config);
	}
	
	@Test
	@Retry
	public void pageAdvancedSearchTest() throws Exception {
		run("Search with fccn", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});
		
		run("Click on advanced search link to navigate to advanced search page",
				() -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"search-form-advanced\"]/button")).click());
		
		appendError(() -> {
			assertEquals("Check if search words maintain fccn term", "fccn",
					waitUntilElementIsVisibleAndGet(By.id("words")).getAttribute("value"));
		});

		run("Insert 31 may 2010 on start date picker", () -> DatePicker.setStartDatePicker(driver, "31/05/2010"));

		run("Insert 1 jan 2019 on end date picker", () -> DatePicker.setEndDatePicker(driver, "01/01/2019"));
		
		appendError("Unselect 'All formats'", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("input[type=checkbox][format=all]")).click());

        appendError("Set format type to 'PDF'", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("input[type=checkbox][format=pdf]")).click());

		appendError("Set site", () -> waitUntilElementIsVisibleAndGet(By.id("website")).sendKeys("fccn.pt"));
		
		appendError("Click on search on arquivo.pt button", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"advanced-search-form-pages\"]/fieldset/section[2]/button")).click());
		
		appendError(() -> assertEquals("After advanced search check search term contains",
				"fccn site:fccn.pt type:pdf",
				waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));
		
		waitUntilElementIsVisibleAndGet(By.id("pages-results"));

		appendError(() -> assertEquals("Verify that all returned mimetypes are PDF", true,
			driver.findElements(By.cssSelector("#pages-results span.mime")).stream().allMatch((x) -> x.getText().toLowerCase().contains("[pdf]"))));

		appendError(() -> assertEquals("Verify that fccn.pt is present in all returned URLs", true,
			driver.findElements(By.cssSelector("#pages-results > ul > li:nth-child(2)")).stream().allMatch((x) -> x.getText().toLowerCase().contains("fccn.pt"))));
		
		// start date - from
		appendError(() -> assertEquals("After advanced search check day start date contains", "20100531",
			new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated((By.id("start-date")))).getAttribute("value").trim()
		));
		// until - end date
		appendError(() -> assertEquals("After advanced search check day end date contains", "20190101",
			new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated((By.id("end-date")))).getAttribute("value").trim()
		));
	}

}
