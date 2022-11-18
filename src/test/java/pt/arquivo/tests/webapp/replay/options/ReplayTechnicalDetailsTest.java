package pt.arquivo.tests.webapp.replay.options;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.time.Duration;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
public class ReplayTechnicalDetailsTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayTechnicalDetailsTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	public String getBaseServiceUrl() {
		return this.testURL.replace("http://", "https://");
	}

	public String getBaseServiceHost() {
		try {
			return new URL(getBaseServiceUrl()).getHost();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error getting host of the test url", e);
		}
	}

	@Test
	@Retry
	public void replayTecnicalDetailsTest() {
		driver.get(this.testURL + WAYBACK_EXAMPLE);

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click on tecnical details anchor", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#a_moreinfo > h4:nth-child(1)")).click());

		waitUntilElementIsVisibleAndGet(By.id("uglipop_popbox"));
		
		appendError(() -> assertEquals("Check originalURL", "http://www.fccn.pt/",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[1]/a")).getText().trim()));

		appendError(() -> assertEquals("Check linkToArchive", this.testURL + WAYBACK_EXAMPLE,
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[2]/a")).getText().trim()));

		appendError(() -> assertEquals("Check tstamp", "tstamp: 19961013145650",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[3]")).getText().trim()));

		appendError(() -> assertEquals("Check contentLength", "contentLength: 3760",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[4]")).getText().trim()));

		appendError(() -> assertEquals("Check digest", "digest: b5f96e1014f99bbd9ef0277cde883f37",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[5]")).getText().trim()));

		appendError(() -> assertEquals("Check mimeType", "mimeType: text/html",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[6]")).getText().trim()));

		appendError(() -> assertEquals("Check encoding", "encoding: windows-1252",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[7]")).getText().trim()));

		appendError(() -> assertEquals("Check date", "date: 0845218610",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[8]")).getText().trim()));

		appendError(() -> assertEquals("Check linkToScreenshot", this.testURL + "/screenshot?url=https%3A%2F%2Fpreprod.arquivo.pt%2FnoFrame%2Freplay%2F19961013145650%2Fhttp%3A%2F%2Fwww.fccn.pt%2F",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[9]/a")).getText().trim()));

		appendError(() -> assertEquals("Check linkToNoFrame:", this.testURL + "/noFrame/replay/19961013145650/http://www.fccn.pt/",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[10]/a")).getText().trim()));

		appendError(() -> assertEquals("Check linkToExtractedText", this.testURL + "/textextracted?m=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[11]/a")).getText().trim()));

		appendError(() -> assertEquals("Check linkToMetadata", this.testURL + "/textsearch?metadata=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[12]/a")).getText().trim()));

		appendError(() -> assertEquals("Check linkToOriginalFile", this.testURL + "/noFrame/replay/19961013145650id_/http://www.fccn.pt/",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[13]/a")).getText().trim()));

		appendError(() -> assertEquals("Check snippet", "snippet: Fundação para a Computação Científica Nacional \" A promoção de infraestruturas no domínio da ...",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[14]")).getText().trim()));

		appendError(() -> assertEquals("Check fileName", "fileName: AWP-Roteiro-20090510220155-00000",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[15]")).getText().trim()));

		appendError(() -> assertEquals("Check collection", "Roteiro",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[16]/a")).getText().trim()));

		appendError(() -> assertEquals("Check offset", "offset: 45198",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[17]")).getText().trim()));

		appendError(() -> assertEquals("Check statusCode", "statusCode: 200",
				waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[18]")).getText().trim()));

		appendError("Close technical detail modal", () -> waitUntilElementIsVisibleAndGet(By.id("removeModal")).click());

		appendError("Check that tecnical details modal is closed when clicking on close button",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_popbox"))));
	}
}