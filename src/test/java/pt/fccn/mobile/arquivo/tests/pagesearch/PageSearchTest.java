package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class PageSearchTest extends WebDriverTestBaseParallel {

	public PageSearchTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void pageSearchTest() {
		pageSearch("fccn collection:Roteiro", "Cerca de 59 resultados desde 1991 até 2021");
	}
	
	public void pageSearch(String query, String numberResults) {
		
		run("Search fccn", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys(query);
			driver.findElement(By.id("submit-search")).click();
		});
	
		
		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]"));
		
		appendError(() -> assertEquals("Verify if the estimated results count message is displayed on page search", numberResults,
				driver.findElement(By.id("estimated-results-value")).getText()));
		
		int anchorsCount = driver.findElementsByXPath("//*[@class=\"page-search-result\"]//*[@class=\"results-url blockUrl\"][contains(text(),'fccn')]")
				.size();
	
		System.out.println("anchorsCount " + anchorsCount);
		
		long emsCount = driver.findElementsByXPath("//*[@class=\"page-search-result\"]//em") //
				.stream() //
				.filter(em -> em.getText().toLowerCase().contains("fccn")) //
				.count();
	
		System.out.println("emsCount " + emsCount);
	
		assertTrue("At least 80 percent of results should show something related to search criteria",
				emsCount + anchorsCount >= 10);
	}

}
