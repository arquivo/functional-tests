package pt.arquivo.tests.webapp.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author ivo.branco@fccn.pt
 *
 */

public class ImageSearchTest extends WebDriverTestBaseParallel {

    /**
     * Test the search of one term in the index interface.
     */
    public ImageSearchTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
    }

    @Test
    @Retry
    public void testImageSearchOneTerm() throws Exception {

        run("Search FCCN term", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Search images instead of text", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-form-images")).click();
        });

        //Get estimated-results-value
        String element = waitUntilElementIsVisibleAndGet(By.id("estimated-results")).getText().trim();
        String[] elements = element.split(" ");
        Double number_of_display_results = Double.valueOf(elements[2]);

        assertTrue("Verify if the estimated results count message is displayed on image search is greater than 2.200",
                number_of_display_results >= 1.000);

        run("Click/open one image on search results to open image viewer",
                () -> waitUntilElementIsVisibleAndGet(By.id("image-card-1")).click());

        appendError(() -> {
            assertTrue("First image details should be shown after clicking on it",
                    waitUntilElementIsVisibleAndGet(By.id("modal")).isDisplayed());
        });

        appendError(() -> {
            assertEquals("Check image name in image viewer", "FCCN",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[1]/p/a")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check image original link in image viewer", "http://www.fccn.eu/images/announce/thumbs/020_58137.jpg", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[3]/p/span")).getText().trim());
        });

        // appendError(() -> {
        // 	assertEquals("Check image type and size on opened modal", "jpeg 319 x 69", driver
        // 			.findElement(By.xpath("//*[@id=\"card0\"]/ion-card-content[1]/ion-list/ion-item[3]")).getText().trim());
        // });

        appendError(() -> {
            assertEquals("Check image capture date in image viewer", "4 Setembro 17h36, 2009", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[4]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page title in image viewer", "FCCN - Fundação para a Computação Científica Nacional",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[7]/p/a")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page URL in image viewer", "http://www.fccn.eu/", driver
                .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[8]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page capture date in image viewer", "4 Setembro 17h36, 2009", driver
                .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[4]/p/span")).getText().trim());
        });

        appendError(() -> { // Click in Details button
            waitUntilElementIsVisibleAndGet(By.id("image-details-button")).click();
        });

        /**************************/
        /*** Details attributes ***/
        /**************************/

        appendError(() -> {
            assertThat("Check image detail page contains page timestamp",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("a6e8551fe818ebddbcc791f424c765aa95e22d5ed6a8960be6ce58cb5e7633cd"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains page timestamp",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("Apresentados os recentes desenvolvimentos da RCTS A FCCN contou com a presença do Ministro da C"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains page timestamp",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("https://arquivo.pt/wayback/20090904173557/http://www.fccn.eu/"));
        });

        /**************************/

        run("Click copy API details ", () -> {
            waitUntilElementIsVisibleAndGet(By.id("copy-raw-api-data")).click();
        });

        run("Close image first modal", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"close-modal-tecnhical\"]/button")).click();
        });
    }
}
