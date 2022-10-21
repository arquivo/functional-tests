//package pt.fccn.mobile.arquivo.tests.imagesearch;
//
//import static org.hamcrest.CoreMatchers.containsString;
//import static org.hamcrest.MatcherAssert.assertThat;
//
//import org.junit.Test;
//import org.openqa.selenium.By;
//
//import pt.fccn.arquivo.selenium.Retry;
//import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
//
///**
// *
// * @author Ivo Branco <ivo.branco@fccn.pt>
// *
// */
//public class ImageSearchQuerySuggestionTest extends WebDriverTestBaseParallel {
//
//	public ImageSearchQuerySuggestionTest(String os, String version, String browser, String deviceName,
//			String deviceOrientation) {
//		super(os, version, browser, deviceName, deviceOrientation, automationName);
//	}
//
//	@Test
//	@Retry
//	public void imageSearchQuerySuggestionTest() {
//		run("Search with testre", () -> {
//			driver.findElement(By.id("txtSearch")).clear();
//			driver.findElement(By.id("txtSearch")).sendKeys("testre");
//			driver.findElement(By.xpath("//*[@id=\"buttonSearch\"]/button")).click();
//		});
//
//		run("Search images instead of text", () -> {
//			waitUntilElementIsVisibleAndGet(By.id("ImageButton")).click();
//		});
//
//		assertThat("Verify if a suggestion is presented",
//				driver.findElement(By.xpath("//*[@class=\"suggestion\"]/a")).getText(), containsString("teste"));
//	}
//
//}
