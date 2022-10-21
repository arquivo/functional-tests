package pt.fccn.mobile.arquivo.tests.replay;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;


/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayTest extends WebDriverTestBaseParallel {

	public ReplayTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void replayTest() throws Exception {

		//Check FCCN Replay Page
		driver.get(this.testURL + "/wayback/19961013145650/http://www.fccn.pt/");

		driver.switchTo().frame("replay_iframe");

		assertThat("Verify if the term RCCN is displayed on the FCCN web page",
				driver.findElement(By.xpath("/html/body/blockquote[1]/h1/a")).getText().toLowerCase(), containsString("rccn"));

		//Check FCCN Replay Page
		driver.get(this.testURL + "/wayback/19961013171554/http://www.fccn.pt/index_i.html");
			
		driver.switchTo().frame("replay_iframe");

		assertThat("Verify if the term Portuguese is displayed on the FCCN web page",
		
		driver.findElement(By.xpath("/html/body/b[1]")).getText().toLowerCase(), containsString("portuguese"));

		//Check Uminho Replay Page

		driver.get(this.testURL + "/wayback/19961013145852/http://s700.uminho.pt:80/homepage-pt.html");
			
		driver.switchTo().frame("replay_iframe");

		assertThat("Verify if the term Portugal is displayed on the Uminho web page",
				driver.findElement(By.xpath("/html/body/center[1]/h1")).getText().toLowerCase(), containsString("portugal"));

		//Check ISCT Replay Page
		driver.get(this.testURL + "/wayback/19961013202814/http://www.iscte.pt/");

		driver.switchTo().frame("replay_iframe");

		assertThat("Verify if the term ISCTE is displayed on the ISCTE web page",
				driver.findElement(By.xpath("/html/body/center[2]/table/tbody/tr/td[1]/h1/center")).getText().toLowerCase(), containsString("iscte"));

		//Check IST Replay Page

		driver.get(this.testURL + "/wayback/19961013171626/http://www.ist.utl.pt/");

		driver.switchTo().frame("replay_iframe");

		assertThat("Verify if the term IST is displayed on the IST web page",
				driver.findElement(By.xpath("/html/body/p[1]/b")).getText().toLowerCase(), containsString("ist"));

	}
}
