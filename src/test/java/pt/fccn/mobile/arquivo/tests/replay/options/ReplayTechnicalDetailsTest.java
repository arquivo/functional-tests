package pt.fccn.mobile.arquivo.tests.replay.options;

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

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class ReplayTechnicalDetailsTest extends WebDriverTestBaseParallel {

	private static final String WAYBACK_EXAMPLE = "/wayback/19961013145650/http://www.fccn.pt/";

	public ReplayTechnicalDetailsTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
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

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("nav-options-right-button")).click());

		run("Click on tecnical details anchor", () -> waitUntilElementIsVisibleAndGet(By.id("menuTechnicalDetails")).click());

		waitUntilElementIsVisibleAndGet(By.id("technical-details"));
		
		appendError(() -> assertEquals("Check originalURL", "http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[3]/a")).getText()));

		appendError(() -> assertEquals("Check linkToArchive", this.testURL + WAYBACK_EXAMPLE,
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[4]/a")).getText()));

		appendError(() -> assertEquals("Check tstamp", "tstamp:19961013145650",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[5]")).getText()));

		appendError(() -> assertEquals("Check contentLength", "contentLength:3760",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[6]")).getText()));

		appendError(() -> assertEquals("Check digest", "digest:b5f96e1014f99bbd9ef0277cde883f37",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[7]")).getText()));

		appendError(() -> assertEquals("Check mimeType", "mimeType:text/html",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[8]")).getText()));

		appendError(() -> assertEquals("Check encoding", "encoding:windows-1252",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[9]")).getText()));

		appendError(() -> assertEquals("Check date", "date:0845218610",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[10]")).getText()));

		appendError(() -> assertEquals("Check linkToScreenshot", this.testURL + "/screenshot?url=https%3A%2F%2Fpreprod.arquivo.pt%2FnoFrame%2Freplay%2F19961013145650%2Fhttp%3A%2F%2Fwww.fccn.pt%2F",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[11]/a")).getText()));

		appendError(() -> assertEquals("Check linkToNoFrame:", this.testURL + "/noFrame/replay/19961013145650/http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[12]/a")).getText()));

		appendError(() -> assertEquals("Check linkToExtractedText", this.testURL + "/textextracted?m=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[13]/a")).getText()));

		appendError(() -> assertEquals("Check linkToMetadata", this.testURL + "/textsearch?metadata=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[14]/a")).getText()));

		appendError(() -> assertEquals("Check linkToOriginalFile", this.testURL + "/noFrame/replay/19961013145650id_/http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[15]/a")).getText()));

		appendError(() -> assertEquals("Check snippet", "snippet:Funda&ccedil;&atilde;o para a Computa&ccedil;&atilde;o Cient&iacute;fica Nacional &quot; A promo&ccedil;&atilde;o de infraestruturas no dom&iacute;nio da<span class=\"ellipsis\"> ... </span>",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[16]")).getText()));

		appendError(() -> assertEquals("Check fileName", "fileName:AWP-Roteiro-20090510220155-00000",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[17]")).getText()));

		appendError(() -> assertEquals("Check collection", "Roteiro",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[18]/a")).getText()));

		appendError(() -> assertEquals("Check offset", "offset:45198",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[19]")).getText()));

		appendError(() -> assertEquals("Check statusCode", "statusCode:200",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[20]")).getText()));

		appendError(() -> assertEquals("Check id", "id:87",
				driver.findElement(By.xpath("//*[@id=\"technical-details\"]/ul/li[21]")).getText()));

		appendError("Close technical detail modal", () -> driver.findElement(By.xpath("//*[@id=\"technical-details\"]/button")).click());

		appendError("Check that tecnical details modal is closed when clicking on close button",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.id("technical-details"))));
	}
}