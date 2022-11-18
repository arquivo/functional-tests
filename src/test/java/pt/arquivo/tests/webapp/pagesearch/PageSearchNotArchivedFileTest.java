package pt.arquivo.tests.webapp.pagesearch;

import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchNotArchivedFileTest extends WebDriverTestBaseParallel {

	public PageSearchNotArchivedFileTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	private boolean isPageArchived(){
		return driver.findElements(By.cssSelector("head, #pageIsNotArchived")).stream().count() == 1;
	}

	private void waitForWaybackThenRun(Runnable r){
		appendError("Wait for wayback page to load", () -> {
			new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#pageIsNotArchived, #replay_iframe")));
			r.run();
			});
	}

	@Test
	@Retry
	public void notArchivedFileTest() throws Exception {
		run("Search with Lisboa", () -> {
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
			waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
			waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
		});

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]"));

		run("click first position", () -> {
			waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).click();
		});

		waitForWaybackThenRun(() -> assertTrue("Verify that first result is archived", isPageArchived()));

		driver.navigate().back();

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]"));

		run("click second position", () -> {
			waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[2]")).click();
		});

		waitForWaybackThenRun(() -> assertTrue("Verify that second result is archived", isPageArchived()));

		driver.navigate().back();

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]"));

		run("click forth position", () -> {
			waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[4]")).click();
		});

		waitForWaybackThenRun(() -> assertTrue("Verify that fourth result is archived", isPageArchived()));

	}

}
