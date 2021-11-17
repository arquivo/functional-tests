package pt.fccn.mobile.arquivo.tests.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParalell;

/**
 *
 * @author ivo.branco@fccn.pt
 *
 */

public class ImageSearchTest extends WebDriverTestBaseParalell {

    /**
     * Test the search of one term in the index interface.
     */
    public ImageSearchTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void testImageSearchOneTerm() throws Exception {

        run("Search FCCN term", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys("fccn");
            driver.findElement(By.id("submit-search")).click();
        });

        run("Search images instead of text", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-form-images")).click();
        });

        //Get estimated-results-value
        String element = driver.findElement(By.id("estimated-results")).getText();
        String[] elements = element.split(" ");
        Double number_of_display_results = Double.valueOf(elements[2]);

        assertTrue("Verify if the estimated results count message is displayed on image search is greater than 2.200",
                number_of_display_results >= 1.000);

        run("Click/open one image on search results to open image viewer",
                () -> waitUntilElementIsVisibleAndGet(By.id("image-card-1")).click());

        appendError(() -> {
            assertTrue("First image details should be shown after clicking on it",
                    driver.findElement(By.id("modal")).isDisplayed());
        });

        appendError(() -> {
            assertEquals("Check image name in image viewer", "FCCN",
                    driver.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[1]/p/a")).getText());
        });

        appendError(() -> {
        	assertEquals("Check image original link in image viewer", "http://www.fccn.eu/images/announce/thumbs/020_58137.jpg", driver
        			.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[3]/p/span")).getText());
        });

        // appendError(() -> {
        // 	assertEquals("Check image type and size on opened modal", "jpeg 319 x 69", driver
        // 			.findElement(By.xpath("//*[@id=\"card0\"]/ion-card-content[1]/ion-list/ion-item[3]")).getText());
        // });

        appendError(() -> {
        	assertEquals("Check image capture date in image viewer", "4 Setembro 17h36, 2009", driver
        			.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[4]/p/span")).getText());
        });

        // appendError(() -> {
        // 	assertEquals("Check page attributes in image viewer", "wiki.di.uminho.pt/twiki/bin/view/Mestrado/TPI",
        // 			driver.findElement(By.xpath("//*[@id=\"card0\"]/ion-card-content[2]/ion-list/ion-item[2]"))
        // 					.getText());
        // });

        appendError(() -> {
        	assertEquals("Check page title in image viewer", "FCCN - Fundação para a Computação Científica Nacional",
        			driver.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[5]/p/a")).getText());
        });

        appendError(() -> {
        	assertEquals("Check page URL in image viewer", "http://www.fccn.eu/", driver
        			.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[6]/p/span")).getText());
        });

        appendError(() -> {
        	assertEquals("Check page capture date in image viewer", "4 Setembro 17h35, 2009", driver
        			.findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[7]/p/span")).getText());
        });

        // appendError(() -> { // Click in Details button
        // 	driver.findElement(By.xpath("//*[@id=\"card0\"]/ion-row[1]/ion-col[2]/ion-button")).click();
        // });

        // /**************************/
        // /*** Details attributes ***/
        // /**************************/

        // appendError(() -> {
        // 	assertEquals("Check image detail page contains page url",
        // 			"http://wiki.di.uminho.pt/twiki/bin/view/Mestrado/TPI",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[1]/ion-list/ion-item[1]/h5/a"))
        // 					.getText());
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[1]/ion-list/ion-item[2]/h5"))
        // 					.getText(),
        // 			containsString("20070707201604"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[1]/ion-list/ion-item[3]/h5"))
        // 					.getText(),
        // 			containsString("Segundo Ciclo em Informática - Universidade do Minho - TWiki"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[1]/h5/a"))
        // 					.getText(),
        // 			containsString("http://wiki.di.uminho.pt/twiki/pub/Mestrado/TPI/fccn.jpg"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[2]/h5"))
        // 					.getText(),
        // 			containsString("20070707201644"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[3]/h5"))
        // 					.getText(),
        // 			containsString("FCCN"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[4]/h5"))
        // 					.getText(),
        // 			containsString("319 x 69 pixels"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[5]/h5"))
        // 					.getText(),
        // 			containsString("jpeg"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(
        // 					By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[2]/ion-list/ion-item[6]/h5"))
        // 					.getText(),
        // 			containsString("0.999"));
        // });

        // appendError(() -> {
        // 	assertThat("Check image detail page contains page timestamp",
        // 			driver.findElement(By.xpath("//*[@id=\"detailsCard0\"]/ion-card-content[3]/ion-list/ion-item/h5"))
        // 					.getText(),
        // 			containsString("IA"));
        // });

        // /**************************/

        // run("Close image details modal", () -> {
        // 	driver.findElement(By.id("closeCard0")).click();
        // });

        // run("Close image first modal", () -> {
        // 	driver.findElement(By.id("close0")).click();
        // });
    }

}
