package pt.arquivo.tests.webapp.citationsaver;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class CitationSaverTest extends WebDriverTestBaseParallel {

    public CitationSaverTest(Map<String, String> config) {
        super(config);
    }
    
    @Test
    @Retry
    public void citationSaverTest() {

        driver.get(this.testURL + "/services/citationsaver");

        run("Wait for citationSaver page", () -> {
            waitUntilElementIsVisibleAndGet(By.id("logo-citation-saver"));
        });

        appendError(() -> assertEquals("Verify slogan from CitationSaver", "Preserva citações a conteúdos online",
            waitUntilElementIsVisibleAndGet(By.id("citation-saver-slogan")).getText().trim()));
        
        appendError(() -> assertEquals("Verify text from CitationSaver", "Submeta um documento e o CitationSaver preservará as ligações nele citadas:",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-main\"]/p[3]")).getText().trim()));

        appendError(() -> assertEquals("Verify text from CitationSaver", "Insira o URL do documento:",
            waitUntilElementIsVisibleAndGet(By.cssSelector("form label[for=\"input-url-url\"]")).getText().trim()));

        run("Click on the 'Ficheiros' tab", () -> {
            Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();
            String platform = capabilities.getPlatformName().name();
            if (platform.toLowerCase().equals("ios")){
                // IOS driver is dumb and sometimes fails to click properly. So instead we use javascript to click.
                waitUntilElementIsVisibleAndGet(By.cssSelector("label[for=\"input-file-upload\"]"));
                ((JavascriptExecutor) driver).executeScript("document.querySelector('label[for=\"input-file-upload\"]').click()");
                
            } else {
                waitUntilElementIsVisibleAndGet(By.cssSelector("label[for=\"input-file-upload\"]")).click();
            }
        });

        appendError(() -> assertEquals("Verify text from CitationSaver", "Carregue um documento a partir do seu computador:",
            waitUntilElementIsVisibleAndGet(By.cssSelector("form label[for=\"input-file-file\"]")).getText().trim()));

    }

}
