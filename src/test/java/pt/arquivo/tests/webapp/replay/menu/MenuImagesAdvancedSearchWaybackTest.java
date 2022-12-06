package pt.arquivo.tests.webapp.replay.menu;

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
public class MenuImagesAdvancedSearchWaybackTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuImagesAdvancedSearchWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void menuImagesAdvancedSearchWaybackTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Click menu button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#menuButton > span.headerMenuText")).click());

		run("Open images sub menu", 
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#imagesMenu > h4:nth-child(1)")).click());

		run("Click new advanced search button",
				() ->  waitUntilElementIsVisibleAndGet(By.cssSelector("#imageOptions > a:nth-child(2) > h4:nth-child(1)")).click());

		appendError("Check if current url is the advanced image search",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/image/advanced/search?")));
	}

}