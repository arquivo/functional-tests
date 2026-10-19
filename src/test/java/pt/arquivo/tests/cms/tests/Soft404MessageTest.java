package pt.arquivo.tests.cms.tests;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Map;
import java.util.regex.Pattern;

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
			
			// the link is added by a script after it finds the page on Arquivo.pt
			WebElement we = new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[@id=\"post-9818\"]/div/div/p[2]/div/a")));

			appendError(() -> assertThat("Verify text from Arquivo.pt link", we.getText(),
					anyOf(containsString("Visite uma versão anterior desta página"),
							containsString("Visit an earlier version of this page"))));

			// the archived version linked can change, so don't check its timestamp
			String href = we.getAttribute("href");
			appendError(() -> assertTrue("Check link to wayback, was " + href,
					href.matches("https://arquivo\\.pt/wayback/\\d{14}mp_/http://sobre\\.arquivo\\.pt"
							+ Pattern.quote(WAYBACK_404_PAGE_EXAMPLE))));
			
			//Test search
			appendError(() -> assertThat(driver.findElement(By.id("messageSearch")).getText(),
					anyOf(equalTo("Experimente pesquisar:"), equalTo("Maybe try searching?"))));
			
			appendError("Check if page is not archived", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
					.until(ExpectedConditions.visibilityOfElementLocated(By.id("___gcse_2"))));
			
		}
	}
}