package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchTest extends WebDriverTestBaseParallel {

	public PageSearchTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void pageSearchTest() {
		pageSearch("fccn collection:Roteiro");
	}
	
// 	

	public void pageSearch(String query) {
		
		run("Search fccn", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(query);
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});
	
		
		waitUntilElementIsVisibleAndGet(By.id("pages-results"));
		
		appendError(() -> assertThat("Verify if the estimated results count message is displayed on page search", 
			waitUntilElementIsVisibleAndGet(By.id("estimated-results-value")).getText().trim(),
			CoreMatchers.allOf(
				CoreMatchers.containsString("Cerca de "),
				CoreMatchers.containsString(" resultados desde 1991 até "+Calendar.getInstance().get(Calendar.YEAR))
			)
		));
		
		long totalResults = driver.findElements(By.cssSelector(".page-search-result")).stream().count();
		long relevantResults = driver.findElements(By.cssSelector(".page-search-result")).stream().filter(em -> em.getText().toLowerCase().contains("fccn")).count();
		
		System.out.println("RelevantResults / totalResults: " + relevantResults + " / " + totalResults);
	
		assertTrue("At least 80 percent of results should show something related to search criteria",
				10*relevantResults >= 8*totalResults); 
	}

}
