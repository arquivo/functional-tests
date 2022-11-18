package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.stream.Stream;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchTest extends WebDriverTestBaseParallel {

	public PageSearchTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void pageSearchTest() {
		pageSearch("fccn collection:Roteiro", "Cerca de 59 resultados desde 1991 até " + Calendar.getInstance().get(Calendar.YEAR));
	}
	
	public void pageSearch(String query, String numberResults) {
		
		run("Search fccn", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(query);
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});
	
		
		waitUntilElementIsVisibleAndGet(By.id("pages-results"));
		
		appendError(() -> assertEquals("Verify if the estimated results count message is displayed on page search", numberResults,
				waitUntilElementIsVisibleAndGet(By.id("estimated-results-value")).getText().trim()));
		
		long totalResults = driver.findElements(By.cssSelector(".page-search-result")).stream().count();
		long relevantResults = driver.findElements(By.cssSelector(".page-search-result")).stream().filter(em -> em.getText().toLowerCase().contains("fccn")).count();
		
		System.out.println("RelevantResults / totalResults: " + relevantResults + " / " + totalResults);
	
		assertTrue("At least 80 percent of results should show something related to search criteria",
				10*relevantResults >= 8*totalResults); 
	}

}
