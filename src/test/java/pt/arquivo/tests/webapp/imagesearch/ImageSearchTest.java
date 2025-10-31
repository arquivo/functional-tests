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

        // We force the collection to ensure every environment (dev, preprod, prod) retrieves the same data
        run("Search Publico term with collection Roteiro", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("Publico collection:Roteiro");
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Search images instead of text", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-form-images")).click();
        });

        //Get estimated-results
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
            assertEquals("Check image alt text in image viewer", "PUBLICO",
                    waitUntilElementIsVisibleAndGet(By.cssSelector("#modal > section > section.image-details-description > div:nth-child(4) > p > span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check image original link in image viewer", "http://shiva.di.uminho.pt:80/~pinj/0/6/20.gif", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[3]/p/span")).getText().trim());
        });

        // appendError(() -> {
        // 	assertEquals("Check image type and size on opened modal", "jpeg 319 x 69", driver
        // 			.findElement(By.xpath("//*[@id=\"card0\"]/ion-card-content[1]/ion-list/ion-item[3]")).getText().trim());
        // });

        appendError(() -> {
            assertEquals("Check image capture date in image viewer", "13 Outubro 15h00, 1996", driver
                    .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[4]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page title in image viewer", "Jose Miranda - HOME PAGE",
                    waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[7]/p/a")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page URL in image viewer", "http://shiva.di.uminho.pt:80/~pinj/", driver
                .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[8]/p/span")).getText().trim());
        });

        appendError(() -> {
            assertEquals("Check page capture date in image viewer", "13 Outubro 14h59, 1996", driver
                .findElement(By.xpath("//*[@id=\"modal\"]/section/section[3]/div[9]/p/span")).getText().trim());
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
                    containsString("19961013150044"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains image caption",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("PÚBLICO ON-LINE é um projecto experimental fruto da colaboração entre o PÚBLICO e o Departamento de Informática da Faculdade"));
        });

        appendError(() -> {
            assertThat("Check image detail page contains page link to archive",
                    waitUntilElementIsVisibleAndGet(
                            By.xpath("//*[@id=\"modal-window-image-technical-details\"]/div[2]/p")).getText().trim(),
                    containsString("arquivo.pt/wayback/19961013145930/http://shiva.di.uminho.pt:80/~pinj/"));
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
