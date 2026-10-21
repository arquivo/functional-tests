package pt.arquivo.tests.webapp.replay;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Duration;
import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;


/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayTest extends WebDriverTestBaseParallel {

	public ReplayTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void replayTest() throws Exception {

		//Check FCCN Replay Page
		driver.get(this.testURL + "/wayback/19961013145650/http://www.fccn.pt/");

		run("Switch context to iframe", () -> new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("replay_iframe"))));

		assertThat("Verify that the term RCCN is displayed on the FCCN web page",
			getTextContentUsingJs("body > blockquote:nth-child(5) > h1 > a").toLowerCase(), containsString("rccn"));
		//Check Uminho Replay Page

		driver.get(this.testURL + "/wayback/19961013145852/http://s700.uminho.pt:80/homepage-pt.html");
			
		run("Switch context to iframe", () -> new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("replay_iframe"))));

		assertThat("Verify if the term Portugal is displayed on the Uminho web page",
			getTextContentUsingJs("body > center:nth-child(1) > h1").toLowerCase(), containsString("portugal"));

		//Check Uminho Replay Page

		driver.get(this.testURL + "/wayback/19961013145852/http://www.uminho.pt/");

		run("Switch context to iframe", () -> new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("replay_iframe"))));

		assertThat("Verify if the term Minho is displayed on the Uminho web page",
			getTextContentUsingJs("body > h2").toLowerCase(), containsString("minho"));

	}

	/** 
	 * The driver for desktop Safari doesn't work properly after switching to iframes, see https://github.com/nightwatchjs/nightwatch/issues/2943
	 * 		I found out that we could work around this problem using a <code>JavascriptExecutor</code> to run javascript that uses <code>document.querySelector</code>
	 * 		instead of using a <code>WebElement</code>. This function is an implementation of the workaround.
	 * */ 
	private String getTextContentUsingJs(String cssSelector){
		return ((JavascriptExecutor) driver).executeScript("return document.querySelector('"+cssSelector+"').textContent").toString();
	}
}
