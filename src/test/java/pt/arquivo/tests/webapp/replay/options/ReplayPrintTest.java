package pt.arquivo.tests.webapp.replay.options;

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
public class ReplayPrintTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayPrintTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void replayPrintTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click print link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#printOption > h4:nth-child(1)")).click());

		run("Cancel print page", () -> waitUntilElementIsVisibleAndGet(By.id("cancelPopup")).click());

		appendError("Check print confirm page is closed", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_content_fixed"))));

		run("Click again on print link", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#printOption > h4:nth-child(1)")).click());

		run("Print page", () -> waitUntilElementIsVisibleAndGet(By.id("printPage")).click());

		// stop here the test because next step is browser specific
	}

}
