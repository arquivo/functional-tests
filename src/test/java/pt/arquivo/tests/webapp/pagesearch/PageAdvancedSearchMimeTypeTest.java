package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageAdvancedSearchMimeTypeTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchMimeTypeTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void testPageAdvancedSearchMimeType() throws Exception {
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
		
		appendError("Unselect 'All formats'", () -> driver.findElement(By.cssSelector("input[type=checkbox][format=all]")).click());

        appendError("Set format type to 'PDF'", () -> driver.findElement(By.cssSelector("input[type=checkbox][format=pdf]")).click());

		appendError("Click on search on arquivo.pt button", () -> driver.findElement(By.xpath("//*[@id=\"advanced-search-form-pages\"]/fieldset/section[2]/button")).click());
		
		appendError(() -> assertEquals("Verify if the - operator is on text box",
				"fccn type:pdf",
				driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));
		
		assertThat("Verify if the term fccn is displayed on any search result",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[2]/a")).getText().toLowerCase(), containsString("fccn"));
		
		appendError(() -> assertEquals("Check mime of first result", "[PDF]",
				driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[2]/a/span")).getText()));
	}
}
