package pt.fccn.mobile.arquivo.tests.replay.menu;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuImagesAdvancedSearchWaybackTest extends MenuWaybackTest {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuImagesAdvancedSearchWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void menuImagesAdvancedSearchWaybackTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		openMenu();

		run("Open images sub menu", () -> waitUntilElementIsVisibleAndGet(By.id("imagesMenu")).click());

		run("Click new search button",
				() -> waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"imageOptions\"]/a[2]/h4")).click());

		appendError("Check if current url is the image search",
				() -> new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains("/image/advanced/search?")));
	}

}
