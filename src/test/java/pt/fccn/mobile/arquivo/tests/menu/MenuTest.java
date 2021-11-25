package pt.fccn.mobile.arquivo.tests.menu;

import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;

public abstract class MenuTest extends WebDriverTestBaseParallel {

	public MenuTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	protected void openMenu() {
		run("Open left menu", () -> {
			waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click();
			waitUntilElementIsVisibleAndGet(By.id("left-nav"));
		});
	}

}
