package pt.fccn.mobile.arquivo.tests.menu;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.mobile.arquivo.utils.LocaleUtils;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class MenuAboutHomepageTest extends MenuTest {

	public MenuAboutHomepageTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void menuAboutHomepagePTTest() {
		LocaleUtils.changeLanguageToPT(this);
		menuAbout("https://sobre.arquivo.pt/pt/");
	}

	@Test
	@Retry
	public void menuAboutHomepageENTest() {
		LocaleUtils.changeLanguageToEN(this);
		menuAbout("https://sobre.arquivo.pt/en/");
	}

	private void menuAbout(String expectedUrl) {
		openMenu();

		run("Click about button", () -> driver.findElementByXPath("//*[@id=\"menuSwiperSlide\"]/a/h4").click());
				
		//waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"logoContainer\"]")); 
		
		appendError("Check if Arquivo.pt log appears", () -> new WebDriverWait(driver, 100)
				.until(ExpectedConditions.visibilityOfElementLocated(By.id("logoContainer"))));
				
		run("Verify sobre.arquivo.pt", () -> {
			assertEquals("Check if current url is the about page", expectedUrl, driver.getCurrentUrl());
		});

	}

}
