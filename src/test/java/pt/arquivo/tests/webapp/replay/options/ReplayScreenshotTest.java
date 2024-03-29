package pt.arquivo.tests.webapp.replay.options;

import java.time.Duration;
import java.util.Map;

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
public class ReplayScreenshotTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayScreenshotTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void replayScreenshotTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click screenshot page link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#screenshotOption > h4:nth-child(1)")).click());

		run("Cancel take screenshot", () -> waitUntilElementIsVisibleAndGet(By.id("cancelPopup")).click());

		appendError("Check save page as image modal is closed", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_content_fixed"))));

		run("Click again on screenshot link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#screenshotOption > h4:nth-child(1)")).click());

		run("Take screenshot", () -> waitUntilElementIsVisibleAndGet(By.id("takeScreenshot")).click());

		run("Close replay menu ", () -> waitUntilElementIsVisibleAndGet(By.id("closeSpecPopUp")).click());

		run("Wait until Arquivo.pt logo is again displayed",
				() -> waitUntilElementIsVisibleAndGet(By.id("arquivoLogo")));
	}

}
