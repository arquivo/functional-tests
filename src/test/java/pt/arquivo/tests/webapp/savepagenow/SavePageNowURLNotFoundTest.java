package pt.arquivo.tests.webapp.savepagenow;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Map;

import org.hamcrest.CoreMatchers;
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

        appendError(() -> assertThat("Verify text from ArchivePageNow",
            waitUntilElementIsVisibleAndGet(By.cssSelector("#not-found-message > ul > li:nth-child(3)")).getText().trim(),
            CoreMatchers.anyOf(
                CoreMatchers.containsString("Use o SavePageNow para gravar a página em falta"),
                CoreMatchers.containsString("Use o ArchivePageNow para gravar a página em falta"))
        ));

        run("Click on link to ArchivePageNow", () -> {
            waitUntilElementIsVisibleAndGet(By.cssSelector("not-found-message > ul > li:nth-child(3) > a")).click();
        });

        appendError(() -> assertEquals("Check that the query is placed on the search bar", query,
                waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).getAttribute("value").trim()));
        
    }

}
