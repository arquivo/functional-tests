package pt.arquivo.tests.webapp.replay.menu;

import java.time.Duration;
import java.util.Map;

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
public class MenuImagesAdvancedSearchWaybackTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public MenuImagesAdvancedSearchWaybackTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void menuImagesAdvancedSearchWaybackTest() {
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
		

		run("Open images sub menu", 
				() -> waitUntilElementIsVisibleAndGet(By.cssSelector("#imagesMenu > h4:nth-child(1)")).click());

		run("Click new advanced search button",
				() ->  waitUntilElementIsVisibleAndGet(By.cssSelector("#imageOptions > a:nth-child(2) > h4:nth-child(1)")).click());

		appendError("Check if current url is the advanced image search",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.urlContains("/image/advanced/search?")));
	}

}