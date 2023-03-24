package pt.arquivo.tests.webapp.savepagenow;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class SavePageNowURLNotFoundTest extends WebDriverTestBaseParallel {

    public SavePageNowURLNotFoundTest(Map<String, String> config) {
        super(config);
    }

    @Test
    @Retry
    public void savePageNowURLNotFound() {
        savePageNowURLNotFound("ddadfcfe.cdsffds");
    }
    
    private void savePageNowURLNotFound(String query) {

        run("Search ddadfcfe.cdsffds", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys(query);
            waitUntilElementIsVisibleAndGet(By.id("submit-search")).click();
        });

        run("Wait for results not found", () -> {
            waitUntilElementIsVisibleAndGet(By.id("no-results-were-found"));
        });

        appendError(() -> assertEquals("Verify text from Save Page Now", "Use o SavePageNow para gravar a página em falta",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"not-found-message\"]/ul/li[5]")).getText().trim()));

        run("Wait for results not found", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"not-found-message\"]/ul/li[5]/a")).click();
        });

        appendError(() -> assertEquals("Check if fccn is in search box on second page", query,
                waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));
        
    }

}
