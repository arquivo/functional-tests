package pt.arquivo.tests.webapp.citationsaver;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel; 

/**
 *
 * @author Pedro Gomes <pedro.gomes@fccn.pt>
 *
 */
public class CitationSaverTest extends WebDriverTestBaseParallel {

    public CitationSaverTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
        super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
    }
    
    @Test
    @Retry
    public void citationSaverTest() {

        driver.get(this.testURL + "/services/citationsaver");

        run("Wait for citationSaver page", () -> {
            waitUntilElementIsVisibleAndGet(By.id("logo-citation-saver"));
        });

        appendError(() -> assertEquals("Verify slogan from CitationSaver", "Preserva citações a conteúdos online",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-slogan\"]")).getText().trim()));
        
        appendError(() -> assertEquals("Verify text from CitationSaver", "Submeta um documento e o CitationSaver preservará as ligações nele citadas:",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-main\"]/p[3]")).getText().trim()));

        appendError(() -> assertEquals("Verify text from CitationSaver", "Insira o URL do documento:",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-main\"]/div[1]/div[1]/form/label[1]")).getText().trim()));

        run("Wait for results not found", () -> {
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-main\"]/div[1]/label[2]")).click();
        });

        appendError(() -> assertEquals("Verify text from CitationSaver", "Carregue um documento a partir do seu computador:",
            waitUntilElementIsVisibleAndGet(By.xpath("//*[@id=\"citation-saver-main\"]/div[1]/div[2]/form/label[1]")).getText().trim()));

    }

}
