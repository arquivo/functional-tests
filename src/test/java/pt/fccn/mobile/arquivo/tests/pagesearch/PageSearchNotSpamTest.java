package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchNotSpamTest extends WebDriverTestBaseParallel {

	public PageSearchNotSpamTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);

	}

	@Test
	@Retry
	public void pageSearchNotSpamTest() throws Exception {
		run("Search with Lisboa", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("lisboa");
			driver.findElement(By.id("submit-search")).click();
		});

		waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"pages-results\"]"));

		int OLXanchorsCount = driver
				.findElementsByXPath("//*[@class=\"page-search-result\"]//*[@class=\"results-url blockUrl\"][contains(text(),'olx')]").size();

		System.out.println("OLX anchors count " + OLXanchorsCount);

		long OLXemsCount = driver.findElementsByXPath("//*[@class=\"page-search-result\"]") //
				.stream() //
				.filter(em -> em.getText().toLowerCase().contains("olx")) //
				.count();

		System.out.println("OLX word count " + OLXemsCount);

		assertTrue("None of the results should show something related with olx", OLXemsCount + OLXanchorsCount == 0);

	}

}
