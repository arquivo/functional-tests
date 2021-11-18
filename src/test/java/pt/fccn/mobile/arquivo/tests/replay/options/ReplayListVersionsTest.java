package pt.fccn.mobile.arquivo.tests.replay.options;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayListVersionsTest extends WebDriverTestBaseParalell {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayListVersionsTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayListVersionsTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click());

		//run("Get list versions button", () -> waitUntilElementIsVisibleAndGet(By.id("menuListVersions")).click());

		WebElement anchor = run("Get list versions button",
				() -> waitUntilElementIsVisibleAndGet(By.id("menuListVersions")));

		String expectedUrlHref = this.testURL + "/page/search?q=http://www.fccn.pt/";
		
		run("Check list version button to correct page",
				() -> assertThat(anchor.getAttribute("href"), endsWith(expectedUrlHref)));

		run("Click list versions anchor", () -> anchor.click());

		String expectedUrl = this.testURL + "/url/search?q=http%3A%2F%2Fwww.fccn.pt%2F&from=19910806&to=20211117&adv_and=http%3A%2F%2Fwww.fccn.pt%2F";

		run("Check url is on list versions",
				() -> new WebDriverWait(driver, 20).until(ExpectedConditions.urlContains(expectedUrl)));
	}

}
