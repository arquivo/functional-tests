package pt.arquivo.tests.webapp.replay.menu;

import java.time.Duration;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.utils.LocaleUtils;


/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class MenuAboutWaybackTest extends WebDriverTestBaseParallel {

	public MenuAboutWaybackTest(Map<String, String> config) {
		super(config);
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
		

		run("Click about button",
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("div.swiper-slide:nth-child(1) > a:nth-child(9) > h4:nth-child(1)")).click());

		appendError("Check if current url is the about page",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains(expectedUrl)));

	}

}
