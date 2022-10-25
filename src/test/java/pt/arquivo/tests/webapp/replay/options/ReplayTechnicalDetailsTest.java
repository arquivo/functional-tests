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

		run("Open replay right menu", () -> waitUntilElementIsVisibleAndGet(By.id("replayMenuButton")).click());

		run("Click on tecnical details anchor", () -> waitUntilElementIsVisibleAndGet(By.cssSelector("#a_moreinfo > h4:nth-child(1)")).click());

		waitUntilElementIsVisibleAndGet(By.id("uglipop_popbox"));
		
		appendError(() -> assertEquals("Check originalURL", "http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[1]/a")).getText()));

		appendError(() -> assertEquals("Check linkToArchive", this.testURL + WAYBACK_EXAMPLE,
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[2]/a")).getText()));

		appendError(() -> assertEquals("Check tstamp", "tstamp: 19961013145650",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[3]")).getText()));

		appendError(() -> assertEquals("Check contentLength", "contentLength: 3760",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[4]")).getText()));

		appendError(() -> assertEquals("Check digest", "digest: b5f96e1014f99bbd9ef0277cde883f37",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[5]")).getText()));

		appendError(() -> assertEquals("Check mimeType", "mimeType: text/html",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[6]")).getText()));

		appendError(() -> assertEquals("Check encoding", "encoding: windows-1252",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[7]")).getText()));

		appendError(() -> assertEquals("Check date", "date: 0845218610",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[8]")).getText()));

		appendError(() -> assertEquals("Check linkToScreenshot", this.testURL + "/screenshot?url=https%3A%2F%2Fpreprod.arquivo.pt%2FnoFrame%2Freplay%2F19961013145650%2Fhttp%3A%2F%2Fwww.fccn.pt%2F",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[9]/a")).getText()));

		appendError(() -> assertEquals("Check linkToNoFrame:", this.testURL + "/noFrame/replay/19961013145650/http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[10]/a")).getText()));

		appendError(() -> assertEquals("Check linkToExtractedText", this.testURL + "/textextracted?m=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[11]/a")).getText()));

		appendError(() -> assertEquals("Check linkToMetadata", this.testURL + "/textsearch?metadata=http%3A%2F%2Fwww.fccn.pt%2F%2F19961013145650",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[12]/a")).getText()));

		appendError(() -> assertEquals("Check linkToOriginalFile", this.testURL + "/noFrame/replay/19961013145650id_/http://www.fccn.pt/",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[13]/a")).getText()));

		appendError(() -> assertEquals("Check snippet", "snippet: Fundação para a Computação Científica Nacional \" A promoção de infraestruturas no domínio da ...",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[14]")).getText()));

		appendError(() -> assertEquals("Check fileName", "fileName: AWP-Roteiro-20090510220155-00000",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[15]")).getText()));

		appendError(() -> assertEquals("Check collection", "Roteiro",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[16]/a")).getText()));

		appendError(() -> assertEquals("Check offset", "offset: 45198",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[17]")).getText()));

		appendError(() -> assertEquals("Check statusCode", "statusCode: 200",
				driver.findElement(By.xpath("//*[@id=\"uglipop_popbox\"]/div/p[18]")).getText()));

		appendError("Close technical detail modal", () -> driver.findElement(By.id("removeModal")).click());

		appendError("Check that tecnical details modal is closed when clicking on close button",
				() -> new WebDriverWait(driver, Duration.ofSeconds(20))
						.until(ExpectedConditions.invisibilityOfElementLocated(By.id("uglipop_popbox"))));
	}
}