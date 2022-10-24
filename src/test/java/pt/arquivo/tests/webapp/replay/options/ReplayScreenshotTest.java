package pt.arquivo.tests.webapp.replay.options;

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
public class ReplayScreenshotTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayScreenshotTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayScreenshotTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click screenshot page link", () -> waitUntilElementIsVisibleAndGet(By.id("screenshotOption")).click());

		run("Cancel take screenshot", () -> waitUntilElementIsVisibleAndGet(By.id("cancelPopup")).click());

		appendError("Check save page as image modal is closed", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_content_fixed"))));

		run("Click again on screenshot link", () -> waitUntilElementIsVisibleAndGet(By.id("screenshotOption")).click());

		run("Take screenshot", () -> waitUntilElementIsVisibleAndGet(By.id("takeScreenshot")).click());

		run("Close replay menu ", () -> waitUntilElementIsVisibleAndGet(By.id("closeSpecPopUp")).click());

		run("Wait until Arquivo.pt logo is again displayed",
				() -> waitUntilElementIsVisibleAndGet(By.id("arquivoLogo")));
	}

}
