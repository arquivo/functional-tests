package pt.arquivo.tests.webapp.imagesearch;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

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
    public ImageSearchTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void imageSearchOneTermTest() throws Exception {

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
            assertEquals("Check image original link in image viewer", "http://www.iscac.pt/files/footericones/01330444039.png", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[3]/p/span")).getText().trim());
        });

        // appendError(() -> {
        // 	assertEquals("Check image type and size on opened modal", "jpeg 319 x 69", driver
        // 			.findElement(By.xpath("//*[@id=\"card0\"]/ion-card-content[1]/ion-list/ion-item[3]")).getText().trim());
        // });

        appendError(() -> {
            assertEquals("Check image capture date in image viewer", "3 Março 10h12, 2021", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[4]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page title in image viewer", "ISCAC o teu futuro passa por aqui!",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[7]/p/a")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page URL in image viewer", "http://www.iscac.pt/", driver
                .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[8]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page capture date in image viewer", "3 Março 10h08, 2021", driver
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
                    containsString("1ab8d250e521e844e62f2426fb6c7ea3fd63f726a07fcefded094eec44007bf4"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains page timestamp",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("Notícias Regulamento Geral Interno da Unidade de Investigação Consulta Pública Ler mais"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains page timestamp",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("https://arquivo.pt/wayback/20210303100835/http://www.iscac.pt/"));
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
