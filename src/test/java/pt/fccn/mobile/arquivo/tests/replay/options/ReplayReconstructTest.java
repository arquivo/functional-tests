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
public class ReplayReconstructTest extends WebDriverTestBaseParalell {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";
//	private static final String MEMENTO_TIMETRAVEL = "https://timetravel.mementoweb.org/reconstruct/http://www.fccn.pt/";

	public ReplayReconstructTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayReconstructTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click());

		run("Click complete page link", () -> waitUntilElementIsVisibleAndGet(By.id("menuCompleteThePage")).click());

		run("Click cancel complete page", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"complete-the-page\"]/ul/li[3]/button")).click());

		appendError("Check complete page confirm page is closed", () -> new WebDriverWait(driver, 20)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("complete-the-page"))));

		run("Click complete page link", () -> waitUntilElementIsVisibleAndGet(By.id("menuCompleteThePage")).click());

		run("Click cancel complete page", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"complete-the-page\"]/ul/li[2]/button")).click());
	}

}
