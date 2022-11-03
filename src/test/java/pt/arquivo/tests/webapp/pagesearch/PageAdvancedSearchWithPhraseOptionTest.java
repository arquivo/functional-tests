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

public class PageAdvancedSearchWithPhraseOptionTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchWithPhraseOptionTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}
	@Test
	@Retry
	public void testPageAdvancedSearchWithPhraseOption() throws Exception {
		run("Search FCCN term", () -> {
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
		
		appendError("Insert the option on form field", () -> waitUntilElementIsVisibleAndGet(By.id("phrase")).sendKeys("speedmeter"));
		
		appendError("Click on search on arquivo.pt button", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"advanced-search-form-pages\"]/fieldset/section[2]/button")).click());

		appendError(() -> assertEquals("Verify if the - operator is on text box",
				"fccn \"speedmeter\"",
				waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));

		assertThat("Verify if the term fccn is displayed on any search result",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getText().trim(), containsString("FCCN"));
		
		assertThat("Verify if the term fccn is displayed on any search result",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).getText().trim(), containsString("Speedmeter FCCN"));
	}
}
