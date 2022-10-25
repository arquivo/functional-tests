package pt.fccn.mobile.arquivo.tests.pagesearch;

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
	public void pageSearchTestPT() {
		LocaleUtils.changeLanguageToPT(this);
		pageSearchTest("→ Não foram encontrados resultados para a sua pesquisa: ");
	}

	@Test
	@Retry
	public void pageSearchTestEN() {
		LocaleUtils.changeLanguageToEN(this);
		pageSearchTest("→ No results were found for the query: ");
	}

	private void pageSearchTest(String noResultsMessage) {
		run("Search with " + QUERY, () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys(QUERY);
			driver.findElement(By.id("submit-search")).click();
		});

		run("Wait for search results appear", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]")));

		appendError("Check result count should be zero", () -> {
			int count = driver.findElements(By.xpath("//*[@class=\"page-search-result\"]")).size();
			assertEquals("Check result count should be zero", 0, count);
		});

		//By emptyResultMessageBy = By.id("conteudo-pesquisa-erro");
		By emptyResultMessageBy = By.id("conteudo-resultado");

		appendError("Empty result message should be visible",
				() -> ExpectedConditions.visibilityOfElementLocated(emptyResultMessageBy));
		
		appendError(() -> assertThat("Empty result message should contains specific text",
				driver.findElement(By.xpath("//*[@id=\"no-results-were-found\"]")).getText(), containsString(noResultsMessage)));
		
		appendError(() -> assertThat("Empty result message should show search criteria",
				driver.findElement(By.xpath("//*[@id=\"no-results-were-found\"]")).getText(), containsString(QUERY)));
	}

}
