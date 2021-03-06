package pt.fccn.mobile.arquivo.tests.menu;

import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

public abstract class MenuTest extends WebDriverTestBaseParalell {

	public MenuTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	protected void openMenu() {
		run("Open left menu", () -> {
			waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"menuButton\"]")).click();
			waitUntilElementIsVisibleAndGet(By.id("menuSwiperSlide"));
		});
	}

}
