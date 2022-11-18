package pt.arquivo.tests.webapp.narrative;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class NarrativeButtonTest extends WebDriverTestBaseParallel {

    public NarrativeButtonTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
        super(os, version, browser, deviceName, deviceOrientation, automationName);
    }

    @Test
    @Retry
    public void narrativeButtonTest() {

        run("Search fccn", () -> {
            waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).clear();
           waitUntilElementIsVisibleAndGet(By.id("submit-search-input")).sendKeys("fccn");
        });

        run("Search Narrative", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-tools-narrative-button")).click();
        });

        run("Check modal", () -> {
            waitUntilElementIsVisibleAndGet(By.id("confirm-narrative-modal"));
        });

        appendError(() -> assertEquals("Verify text from ContaMeHistorias.pt", "Vai sair do Arquivo.pt para o ContaMeHistorias.pt",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"confirm-narrative-modal\"]/ul/li[1]/p")).getText().trim()));

        run("Check the cancel button", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"confirm-narrative-modal\"]/ul/li[3]/a/button")).click();
        });

        appendError("Check if modal is closed", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id("confirm-narrative-modal"))));

        run("Search Narrative", () -> {
            waitUntilElementIsVisibleAndGet(By.id("search-tools-narrative-button")).click();
        });

        run("Check modal", () -> {
            waitUntilElementIsVisibleAndGet(By.id("confirm-narrative-modal"));
        });

        run("Click on OK button", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"search-form-narrative\"]/button")).click();
        });

        appendError("Verify text from ContaMeHistorias.pt", () -> new WebDriverWait(driver, Duration.ofSeconds(20))
        .until(ExpectedConditions.and(
            ExpectedConditions.urlContains("https://contamehistorias.pt/arquivopt/search"),
            ExpectedConditions.urlContains("query=fccn"),
            ExpectedConditions.urlContains("last_years=10"),
            ExpectedConditions.urlContains("lang=pt")
        )));
        
        
        // assertEquals("Verify text from ContaMeHistorias.pt", "https://contamehistorias.pt/arquivopt/searching?query=fccn&last_years=10&lang=pt",
        //     driver.getCurrentUrl()));
    }

}
