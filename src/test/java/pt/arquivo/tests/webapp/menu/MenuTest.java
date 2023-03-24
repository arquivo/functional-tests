package pt.arquivo.tests.webapp.menu;

import java.util.Map;

import org.openqa.selenium.By;

import pt.arquivo.selenium.WebDriverTestBaseParallel;

public abstract class MenuTest extends WebDriverTestBaseParallel {

	public MenuTest(Map<String, String> config) {
		super(config);
	}

	protected void openMenu() {
		run("Open left menu", () -> {
			waitUntilElementIsVisibleAndGet(By.id("nav-menu-button-left")).click();
			waitUntilElementIsVisibleAndGet(By.id("left-nav"));
		});
	}

}
