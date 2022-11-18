package pt.arquivo.tests.webapp.menu;

import org.openqa.selenium.By;

import pt.arquivo.selenium.WebDriverTestBaseParallel;

public abstract class MenuTest extends WebDriverTestBaseParallel {

	public MenuTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	protected void openMenu() {
		run("Open left menu", () -> {
			waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click();
			waitUntilElementIsVisibleAndGet(By.id("left-nav"));
		});
	}

}
