package pt.fccn.mobile.arquivo.tests.pagesearch;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchNotArchivedFileTest extends WebDriverTestBaseParallel {

	public PageSearchNotArchivedFileTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void notArchivedFileTest() throws Exception {
		run("Search with Lisboa", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
			driver.findElement(By.id("submit-search")).click();
		});

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]"));

		run("click first position", () -> {
			driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[1]")).click();
		});

		appendError("Check if page is not archived", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("pageIsNotArchived"))));

		driver.navigate().back();

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]"));

		run("click second position", () -> {
			driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[2]")).click();
		});

		appendError("Check if page is not archived", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("pageIsNotArchived"))));

		driver.navigate().back();

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]/ul[1]"));

		run("click forth position", () -> {
			driver.findElement(By.xpath("//*[@id=\"pages-results\"]/ul[4]")).click();
		});

		appendError("Check if page is not archived", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("pageIsNotArchived"))));

	}

}
