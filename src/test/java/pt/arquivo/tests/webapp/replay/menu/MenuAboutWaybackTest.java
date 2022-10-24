package pt.arquivo.tests.webapp.replay.menu;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.mobile.arquivo.utils.LocaleUtils;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;


/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class MenuAboutWaybackTest extends WebDriverTestBaseParallel {

	public MenuAboutWaybackTest(String os, String version, String browser, String deviceName,
			String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	@Test
	@Retry
	public void menuAboutWaybackPTTest() {
		LocaleUtils.changeLanguageToPT(this);
		driver.get(this.testURL + WAYBACK_EXAMPLE);
		menuAbout("https://sobre.arquivo.pt/pt/");
	}

	@Test
	@Retry
	public void menuAboutWaybackENTest() {
		LocaleUtils.changeLanguageToEN(this);
		driver.get(this.testURL + WAYBACK_EXAMPLE);
		menuAbout("https://sobre.arquivo.pt/en/");
	}

	private void menuAbout(String expectedUrl) {
		
		System.out.println("Current url: " + driver.getCurrentUrl());

		run("Click menu button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#menuButton > span.headerMenuText")).click());

		run("Click about button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#swiperWrapper > div.swiper-slide.menu.swiper-slide-active > a:nth-child(9)")).click());

		appendError("Check if current url is the about page",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains(expectedUrl)));

	}

}
