package pt.arquivo.tests.webapp.replay.menu;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

import java.time.Duration;
/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class MenuPagesNewAvancedSearchWaybackTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuPagesNewAvancedSearchWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void menuPagesNewAvancedSearchWaybackTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Click menu button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#menuButton > span.headerMenuText")).click());

		run("Open pages sub menu", 
				() -> waitUntilElementIsVisibleAndGet(By.id("pagesMenu")).click());

		run("Click new search button",
				() ->  waitUntilElementIsVisibleAndGet(By.cssSelector("#pageOptions > a:nth-child(2)")).click());

		appendError("Check if current url is the advanced search",
				() -> new WebDriverWait(driver, java.time.Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/page/advanced/search?")));
	}

}
