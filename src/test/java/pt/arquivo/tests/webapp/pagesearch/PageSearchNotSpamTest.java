package pt.arquivo.tests.webapp.pagesearch;

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

public class PageSearchNotSpamTest extends WebDriverTestBaseParallel {

	public PageSearchNotSpamTest(Map<String, String> config) {
		super(config);

	}

	@Test
	@Retry
	public void pageSearchNotSpamTest() throws Exception {
		run("Search with Lisboa", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("lisboa");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]"));

		long OLXCount = driver.findElements(By.cssSelector(".page-search-result")) //
				.stream() //
				.filter(em -> em.getText().toLowerCase().contains("olx")) //
				.count();

		System.out.println("OLX total count " + OLXCount);

		assertTrue("None of the results should show something related with olx", OLXCount  == 0);

	}

}
