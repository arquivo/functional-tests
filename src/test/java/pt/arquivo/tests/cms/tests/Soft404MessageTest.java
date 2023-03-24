package pt.arquivo.tests.cms.tests;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;


/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class Soft404MessageTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_404_PAGE_EXAMPLE = "/about-the-archive/publications-1/documents/peopleware-rarc-article-in-jpg/image_preview";
	
	public Soft404MessageTest(Map<String, String> config) {
		super(config);
		
	}

	@Test
	@Retry
	public void soft404MessageTest() {
		
		//The test does not work in preprod.sobre.arquivo.pt. Since the Arquivo.pt only collect the sobre.arquivo.pt.
		if (!this.testURL.toLowerCase().contains("preprod.sobre.arquivo.pt")) {
			driver.get(this.testURL + WAYBACK_404_PAGE_EXAMPLE);
			
			waitUntilElementIsVisibleAndGet(By.id("post-9818"));
			 
			//The language of the page is not based on the "English or Portugues" button, but depends on the language of the search engine. 
			
			appendError(() -> assertThat("Verify text from Arquivo.pt link",
					driver.findElement(By.xpath("//*[@id=\"post-9818\"]/div/div/p[2]/div/a")).getText(),
					containsString("Visit an earlier version of this page on 27 February, 2017")));

			List<WebElement> wes = driver.findElements(By.xpath("//*[@id=\"post-9818\"]/div/div/p[2]/div/a"));
			WebElement we = wes.get(0);
			String href = we.getAttribute("href");
			assertEquals("Check link to wayback", href, "https://arquivo.pt/wayback/20170227184149mp_/http://sobre.arquivo.pt" + WAYBACK_404_PAGE_EXAMPLE);
			
			//Test search
			appendError(() -> assertEquals("Maybe try searching?",
					driver.findElement(By.id("messageSearch")).getText()));
			
			appendError("Check if page is not archived", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("___gcse_2"))));
			
		}
	}
}