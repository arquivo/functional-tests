package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchQuerySuggestionTest extends WebDriverTestBaseParallel {

	public PageSearchQuerySuggestionTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	@Test
	@Retry
	public void pageSearchQuerySuggestionTest() {
		run("Search with amazoncouk", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("amazoncouk");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

		assertThat("Verify if a suggestion is presented",
				waitUntilElementIsVisibleAndGet(By.id("term-suggested")).getText().trim(), containsString("→ Será que quis dizer: amazon.co.uk"));
	}

}
