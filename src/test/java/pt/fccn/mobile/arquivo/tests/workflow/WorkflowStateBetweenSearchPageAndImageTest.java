package pt.fccn.mobile.arquivo.tests.workflow;

import java.time.LocalDate;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;
import pt.fccn.mobile.arquivo.utils.IonicDatePicker;

/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class WorkflowStateBetweenSearchPageAndImageTest extends WebDriverTestBaseParalell {

	public WorkflowStateBetweenSearchPageAndImageTest(String os, String version, String browser, String deviceName,
			String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
	@Retry
	public void stateBetweenSearchPageAndImageTest() throws Exception {
		run("Search FCCN term", () -> {
			driver.findElement(By.id("txtSearch")).clear();
			driver.findElement(By.id("txtSearch")).sendKeys("fccn");
			driver.findElement(By.xpath("//*[@id=\"buttonSearch\"]/button")).click();
		});

		run("Open from date picker", () -> waitUntilElementIsVisibleAndGet(By.id("sliderCircleStart")).click());
		LocalDate fromDate = LocalDate.of(1997, 5, 20);
		run("Insert " + fromDate.toString() + " on start date picker",
				() -> IonicDatePicker.changeTo(driver, fromDate));

		run("Open until date picker", () -> waitUntilElementIsVisibleAndGet(By.id("sliderCircleEnd")).click());
		LocalDate untilDate = LocalDate.of(2014, 1, 1);
		run("Insert " + untilDate.toString() + " on end date picker",
				() -> IonicDatePicker.changeTo(driver, untilDate));

		run("Click Image Button", () -> {
			driver.findElement(By.xpath("//*[@id=\"ImageButton\"]")).click();
		});

		appendError("Check if fccn is in search box on second page",
				() -> driver.findElement(By.xpath("//*[@value=\"fccn\"]")));

		appendError("Check if sliderLeft is the same on second page", () -> {
			driver.findElement(By.xpath("//*[@id=\"calendarDayStart\"][contains(text(),'20')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarMonthStart\"][contains(text(),'Mai')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarYearStart\"][contains(text(),'1997')]"));
		});

		appendError("Check if sliderRigth is the same on second page", () -> {
			driver.findElement(By.xpath("//*[@id=\"calendarDayEnd\"][contains(text(),'1')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarMonthEnd\"][contains(text(),'Jan')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarYearEnd\"][contains(text(),'2014')]"));
		});

		run("Click Page Button", () -> {
			driver.findElement(By.xpath("//*[@id=\"PageButton\"]")).click();
		});

		appendError("Check if fccn is in search box on first page",
				() -> driver.findElement(By.xpath("//*[@value=\"fccn\"]")));

		appendError("Check if sliderLeft is the same on first page", () -> {
			driver.findElement(By.xpath("//*[@id=\"calendarDayStart\"][contains(text(),'20')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarMonthStart\"][contains(text(),'Mai')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarYearStart\"][contains(text(),'1997')]"));
		});

		appendError("Check if sliderRigth is the same on first page", () -> {
			driver.findElement(By.xpath("//*[@id=\"calendarDayEnd\"][contains(text(),'1')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarMonthEnd\"][contains(text(),'Jan')]"));
			driver.findElement(By.xpath("//*[@id=\"calendarYearEnd\"][contains(text(),'2014')]"));
		});
	}

}
