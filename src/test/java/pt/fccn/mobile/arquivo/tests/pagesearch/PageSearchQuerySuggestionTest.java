package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchQuerySuggestionTest extends WebDriverTestBaseParallel {

	public PageSearchQuerySuggestionTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void pageSearchQuerySuggestionTest() {
		run("Search with lisbo", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("lisbo");
			driver.findElement(By.id("submit-search")).click();
		});

		assertThat("Verify if a suggestion is presented",
				driver.findElement(By.id("term-suggested")).getText(), containsString("→ Será que quis dizer: lisboa"));
	}

}
