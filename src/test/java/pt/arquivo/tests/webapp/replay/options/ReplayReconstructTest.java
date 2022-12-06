package pt.arquivo.tests.webapp.replay.options;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.CustomConditions;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayReconstructTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";
//	private static final String MEMENTO_TIMETRAVEL = "https://timetravel.mementoweb.org/reconstruct/http://www.fccn.pt/";

	public ReplayReconstructTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	@Test
	@Retry
	public void replayReconstructTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click complete page link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#a_reconstruct > h4:nth-child(1)")).click());

		run("Click cancel on the popup", () -> waitUntilElementIsVisibleAndGet(By.id("cancelPopup")).click());

		appendError("Check complete page confirm page is closed", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_content_fixed"))));

		run("Click complete page link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#a_reconstruct > h4:nth-child(1)")).click());

		run("Click confirm on the popup", () -> waitUntilElementIsVisibleAndGet(By.id("completePage")).click());


		//Code below to handle weird behaviour where the web driver wouldn't realize they opened  
		//   CompletePage even though the SauceLabs video clearly displayed it being loaded.
		//   The video would show the CompletePage page, but using something like diver.getCurrentUrl() 
		//   would yield the original Replay page URL, or using waitUntilElementIsVisibleAndGet() wouldn't work on 
		//   elements on the CompletePage page, but would work on elements on the Replay page.
		try {
			new WebDriverWait(driver, Duration.ofSeconds(20)).until(CustomConditions.browserUrlContains("/services/complete-page"));
		} catch (Exception e) {
			if(driver.findElements(By.cssSelector("head, #a_reconstruct")).stream().count() > 1){
				String completePageUrl = waitUntilElementIsVisibleAndGet(By.id("a_reconstruct")).getAttribute("href");
				System.out.println("Web driver stuck. Forcing redirect to: " + completePageUrl);
				driver.get(completePageUrl);
			}
		}
		

		run("Check that we moved to CompletePage service", 
		() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.and(
				CustomConditions.browserUrlContains("/services/complete-page"),
				CustomConditions.browserUrlContains("url=http://www.fccn.pt/"),
				CustomConditions.browserUrlContains("timestamp=19961013145650")
		)));
		
	}

}
