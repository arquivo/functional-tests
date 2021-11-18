package pt.fccn.mobile.arquivo.tests.replay.options;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayScreenshotTest extends WebDriverTestBaseParalell {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayScreenshotTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayScreenshotTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click());

		run("Click screenshot page link", () -> waitUntilElementIsVisibleAndGet(By.id("menuScreenshot")).click());

		run("Cancel take screenshot", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"screenshot\"]/ul/li[3]/button")).click());

		appendError("Check save page as image modal is closed", () -> new WebDriverWait(driver, 20)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("screenshot"))));

		run("Click again on screenshot link", () -> waitUntilElementIsVisibleAndGet(By.id("menuScreenshot")).click());

		run("Take screenshot", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"screenshot\"]/ul/li[2]/button")).click());

		run("Close replay menu ", () -> waitUntilElementIsVisibleAndGet(By.id("right-nav-close-button")).click());

		run("Wait until Arquivo.pt logo is again displayed",
				() -> waitUntilElementIsVisibleAndGet(By.id("logo-arquivo")));
	}

}
