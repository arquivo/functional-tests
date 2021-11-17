package pt.fccn.mobile.arquivo.tests.pagesearch;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;
import pt.fccn.mobile.arquivo.utils.IonicDatePicker;

/**
 * 
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */

public class PageSearchOverlapDatesTest extends WebDriverTestBaseParalell {

	public PageSearchOverlapDatesTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void pageSearchOverlapDatesTest() throws Exception {
		run("Search with fccn", () -> {
			driver.findElement(By.id("submit-search-input")).clear();
			driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
			driver.findElement(By.id("submit-search")).click();
		});

		Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
        String platform = capabilities.getPlatform().name();
		
		run("Open start date picker", () -> driver.findElement(By.id("date-container-start")).click());

		run("Insert 20/05/1997 on start date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("20/05/1997");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

		run("Open end date picker", () -> driver.findElement(By.id("end-year")).click());

        run("Insert 22/08/1996 on end date picker", () -> {
            if (platform == "LINUX" || platform == "WINDOWS")
                waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-input")).sendKeys("22/08/1996");
            else
                System.out.println("TODO: Android test");
        });

        run("Click OK", () -> {
            waitUntilElementIsVisibleAndGet(By.id("modal-datepicker-confirm-button-span")).click();
        });

        LocalDate untilDate = LocalDate.of(1996, 8, 22);

		appendError(() -> {
			assertTrue("Check if it is possible to do date overlap: ", checkDatePicker(untilDate));
		});

	}

	private boolean checkDatePicker(LocalDate untilDate) {
		try {
			IonicDatePicker.changeTo(driver, untilDate);
			return false;
		} catch (Exception e) {
			return true;
		}
	}

}
