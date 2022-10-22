package pt.arquivo.tests.webapp.replay.options;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.tests.webapp.utils.CustomConditions;
import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayExpandTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_SITE = "http://www.fccn.pt/";

	private static final String WAYBACK_PATH = "/wayback/19961013145650/" + WAYBACK_SITE;

	private static final String NOFRAME_EXAMPLE = "/noFrame/replay/19961013145650/" + WAYBACK_SITE;

	public ReplayExpandTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayExpandTest() throws InterruptedException {
		driver.get(this.testURL + WAYBACK_PATH);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click expand link", () -> waitUntilElementIsVisibleAndGet(By.id("expandPage")).click());

		run("Check we go to correct no frame url", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
			.until(CustomConditions.browserUrlContains(NOFRAME_EXAMPLE)));

	}
		
}
