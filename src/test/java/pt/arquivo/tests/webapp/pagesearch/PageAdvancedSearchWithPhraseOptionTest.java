package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageAdvancedSearchWithPhraseOptionTest extends WebDriverTestBaseParallel {

	public PageAdvancedSearchWithPhraseOptionTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}
	@Test
	@Retry
	public void pageAdvancedSearchWithPhraseOptionTest() throws Exception {
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

		waitUntilElementIsVisibleAndGet(By.id("pages-results"));

		assertEquals("Verify if the term fccn is displayed on any search result", true,
			driver.findElements(By.cssSelector("#pages-results > ul > li")).stream().anyMatch((x) -> x.getText().toLowerCase().contains("fccn")));

		assertEquals("Verify if the term 'speedmeter fccn' is displayed on any search result", true,
			driver.findElements(By.cssSelector("#pages-results > ul > li")).stream().anyMatch((x) -> x.getText().toLowerCase().contains("speedmeter fccn")));
							
		
	}
}
