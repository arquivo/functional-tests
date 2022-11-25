package pt.arquivo.tests.webapp.replay.menu;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;


/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class MenuSavePageNowTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuSavePageNowTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	@Test
	@Retry
	public void menuSavePageNowTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Click menu button",
				() -> {
					waitUntilElementIsVisibleAndGet(By.cssSelector("#menuButton"));
					
					if(driver.findElement(By.cssSelector("#menuButton > span")).isDisplayed()){
						// Desktop
						driver.findElement(By.cssSelector("#menuButton > span")).click();
					} else {
						// Mobile
						driver.findElement(By.cssSelector("#menuButton > div")).click();
					}
				});
				

		run("Click SavePageNow button",
				() ->  waitUntilElementIsVisibleAndGet(By.cssSelector("#swiperWrapper > div.swiper-slide.menu.swiper-slide-active > a:nth-child(8) > h4")).click());


		appendError("Check if current url is savepagenow",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/services/savepagenow?")));
	}

}
