package pt.fccn.mobile.arquivo.tests.replay.menu;

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
public class MenuPagesNewSearchWaybackTest extends WebDriverTestBaseParalell {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuPagesNewSearchWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void menuPagesNewSearchWaybackTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Click about button",
				() -> waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click());

		run("Open pages sub menu", 
				() -> waitUntilElementIsVisibleAndGet(By.id("menu-pages")).click());

		run("Click new search button",
				() ->  waitUntilElementIsVisibleAndGet(By.id("menu-pages-new-search")).click());

		appendError("Check if current url is the page search",
				() -> new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains("/page/search?")));
	}

}
