package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageAdvancedSearchMimeTypeTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchMimeTypeTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void pageAdvancedSearchMimeTypeTest() throws Exception {
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
		
		appendError("Unselect 'All formats'", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("input[type=checkbox][format=all]")).click());

        appendError("Set format type to 'PDF'", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("input[type=checkbox][format=pdf]")).click());

		appendError("Click on search on arquivo.pt button", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"advanced-search-form-pages\"]/fieldset/section[2]/button")).click());
		
		appendError(() -> assertEquals("Verify if the - operator is on text box",
				"fccn type:pdf",
				waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));
		
		waitUntilElementIsVisibleAndGet(By.id("pages-results"));

		assertTrue("Verify if the term fccn is displayed on any search result",
			driver.findElements(By.cssSelector("#pages-results > ul > li")).stream().anyMatch((x) -> x.getText().toLowerCase().contains("fccn")));
		
		appendError(() -> assertEquals("Check mime of first result", "[PDF]",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]/li[2]/a/span")).getText().trim()));
	}
}
