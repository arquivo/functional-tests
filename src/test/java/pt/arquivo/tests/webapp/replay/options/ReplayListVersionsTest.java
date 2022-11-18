package pt.arquivo.tests.webapp.replay.options;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.util.Calendar;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayListVersionsTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayListVersionsTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void replayListVersionsTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		WebElement anchor = run("Get list versions button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#swiperWrapper > div.swiper-slide.replayMenu.swiper-slide-next > a:nth-child(2)")));


		run("Check list version button to correct page",
				() -> {
					String href = anchor.getAttribute("href");
					assertThat("Href is searching the right URL ( "+href+" )" , href.contains("query=http%3A%2F%2Fwww.fccn.pt%2F"));
					assertThat("Href is searching in the right place ( "+href+" )", href.contains("/page/search"));
				});
		
		run("Click list versions anchor", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("div.swiper-slide:nth-child(3) > a:nth-child(2) > h4:nth-child(1)")).click());

		String expectedUrl = this.testURL + "/url/search?q=http%3A%2F%2Fwww.fccn.pt%2F&from=19910806";

		run("Check url is on list versions",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.and(
					ExpectedConditions.urlContains("/url/search"),
					ExpectedConditions.urlContains("q=http%3A%2F%2Fwww.fccn.pt%2F"),
					ExpectedConditions.urlContains("from=19910806"),
					ExpectedConditions.urlContains("to="+Calendar.getInstance().get(Calendar.YEAR))
				)));
	}

}
