package pt.arquivo.tests.webapp.menu;

import org.openqa.selenium.By;

import pt.arquivo.selenium.WebDriverTestBaseParallel;

public abstract class MenuTest extends WebDriverTestBaseParallel {

	public MenuTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}

	protected void openMenu() {
		run("Open left menu", () -> {
			waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click();
			waitUntilElementIsVisibleAndGet(By.id("left-nav"));
		});
	}

}
