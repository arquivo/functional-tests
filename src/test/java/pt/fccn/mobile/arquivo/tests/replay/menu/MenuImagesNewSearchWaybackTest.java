package pt.fccn.mobile.arquivo.tests.replay.menu;

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
public class MenuImagesNewSearchWaybackTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuImagesNewSearchWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void menuImagesNewSearchWaybackTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Click about button",
				() -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click());

		run("Open images sub menu", 
				() -> waitUntilElementIsVisibleAndGet(By.id("menu-images")).click());

		run("Click new search button",
				() ->  waitUntilElementIsVisibleAndGet(By.id("menu-images-new-search")).click());
		
		appendError("Check if current url is the image search",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/image/search?")));
	}

}
