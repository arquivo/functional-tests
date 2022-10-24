package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.mobile.arquivo.utils.LocaleUtils;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchEmptyTest extends WebDriverTestBaseParallel {

	private static final String QUERY = "xptoxptoxptoxptoxptoxptoxptoxptoxpto";

	public PageSearchEmptyTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void pageSearchEmptyTestPT() {
		LocaleUtils.changeLanguageToPT(this);
		pageSearchTest("Não foram encontrados resultados para a sua pesquisa: ");
	}

	@Test
	@Retry
	public void pageSearchEmptyTestEN() {
		LocaleUtils.changeLanguageToEN(this);
		pageSearchTest("No results were found for the query: ");
	}

	private void pageSearchTest(String noResultsMessage) {
		run("Search with " + QUERY, () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys(QUERY);
			driver.findElement(By.id("submit-search")).click();
		});

		run("Wait for search results appear", () -> waitUntilElementIsVisibleAndGet(By.id("pages-results")));

		By emptyResultMessageBy = By.id("no-results-were-found");

		appendError("Empty result message should be visible",
				() -> ExpectedConditions.visibilityOfElementLocated(emptyResultMessageBy));
		
		String emptyResultMessage = driver.findElement(emptyResultMessageBy).getText();
		appendError(() -> assertThat("Empty result message should contains specific text",
			emptyResultMessage, containsString(noResultsMessage)));
		
		appendError(() -> assertThat("Empty result message should show search criteria",
			emptyResultMessage, containsString(QUERY)));
	}

}
