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
public class ReplayPrintTest extends WebDriverTestBaseParalell {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayPrintTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayPrintTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click());

		run("Click print link", () -> waitUntilElementIsVisibleAndGet(By.id("menuPrint")).click());

		run("Cancel print page", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"print\"]/ul/li[3]/button")).click());

		appendError("Check print confirm page is closed", () -> new WebDriverWait(driver, 20)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.id("print"))));

		run("Click again on print link", () -> waitUntilElementIsVisibleAndGet(By.id("menuPrint")).click());

		run("Print page", () -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"print\"]/ul/li[2]/button")).click());

		// stop here the test because next step is browser specific
	}

}
