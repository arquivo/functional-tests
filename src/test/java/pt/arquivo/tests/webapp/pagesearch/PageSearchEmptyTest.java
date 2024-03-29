package pt.arquivo.tests.webapp.pagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchEmptyTest extends WebDriverTestBaseParallel {

	private static final String QUERY = "xptoxptoxptoxptoxptoxptoxptoxptoxpto";

	public PageSearchEmptyTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void pageSearchEmptyPTTest() {
		LocaleUtils.changeLanguageToPT(this);
		pageSearchTest("Não foram encontrados resultados para a sua pesquisa: ");
	}

	@Test
	@Retry
	public void pageSearchEmptyENTest() {
		LocaleUtils.changeLanguageToEN(this);
		pageSearchTest("No results were found for the query: ");
	}

	private void pageSearchTest(String noResultsMessage) {
		run("Search with " + QUERY, () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(QUERY);
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

		run("Wait for search results appear", () -> waitUntilElementIsVisibleAndGet(By.id("pages-results")));

		By emptyResultMessageBy = By.id("no-results-were-found");

		appendError("Empty result message should be visible",
				() -> ExpectedConditions.visibilityOfElementLocated(emptyResultMessageBy));
		
		String emptyResultMessage = waitUntilElementIsVisibleAndGet(emptyResultMessageBy).getText().trim();
		appendError(() -> assertThat("Empty result message should contains specific text",
			emptyResultMessage, containsString(noResultsMessage)));
		
		appendError(() -> assertThat("Empty result message should show search criteria",
			emptyResultMessage, containsString(QUERY)));
	}

}
