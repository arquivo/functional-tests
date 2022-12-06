package pt.arquivo.tests.webapp.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
*
* @author Ivo Branco <ivo.branco@fccn.pt>
*
*/
public class ImageSearchQuerySuggestionTest extends WebDriverTestBaseParallel {

	public ImageSearchQuerySuggestionTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	@Test
	@Retry
	public void imageSearchQuerySuggestionTest() {
		run("Search with testre", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("amazoncouk");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

		run("Search images instead of text", () -> {
			waitUntilElementIsVisibleAndGet(By.cssSelector("#search-form-images button")).click();
		});

		assertThat("Verify if a suggestion is presented",
				waitUntilElementIsVisibleAndGet(By.cssSelector("#term-suggested a")).getText().trim(), containsString("amazon.co.uk"));
	}

}
