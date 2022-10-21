package pt.arquivo.tests.webapp.savepagenow;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class SavePageNowURLNotFoundTest extends WebDriverTestBaseParallel {

    public SavePageNowURLNotFoundTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
        super(os, version, browser, deviceName, deviceOrientation);
    }

    @Test
    @Retry
    public void savePageNowURLNotFound() {
        savePageNowURLNotFound("ddadfcfe.cdsffds");
    }
    
    private void savePageNowURLNotFound(String query) {

        run("Search ddadfcfe.cdsffds", () -> {
            driver.findElement(By.id("submit-search-input")).clear();
            driver.findElement(By.id("submit-search-input")).sendKeys(query);
            driver.findElement(By.id("submit-search")).click();
        });

        run("Wait for results not found", () -> {
            waitUntilElementIsVisibleAndGet(By.id("no-results-were-found"));
        });

        appendError(() -> assertEquals("Verify text from Save Page Now", "Use o SavePageNow para gravar a página em falta",
            driver.findElement(By.xpath("//*[@id=\"not-found-message\"]/ul/li[5]")).getText()));

        run("Wait for results not found", () -> {
            driver.findElement(By.xpath("//*[@id=\"not-found-message\"]/ul/li[5]/a")).click();
        });

        appendError(() -> assertEquals("Check if fccn is in search box on second page", query,
                driver.findElement(By.id("submit-search-input")).getAttribute("value").trim()));
        
    }

}
