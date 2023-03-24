package pt.arquivo.tests.contamehistorias;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.openqa.selenium.By;

import pt.arquivo.selenium.WebDriverTestBaseParallel;

/**
 *
 * @author Ivo Branco <ivo.branco@fccn.pt>
 *
 */
public class ContaMeHistoriasTest extends WebDriverTestBaseParallel {

	public ContaMeHistoriasTest(Map<String, String> config) {
		super(config);
	}

	@Test
	public void testImageSearchOneTerm() {
		driver.get("http://contamehistorias.pt");

		appendError(() -> {
			assertTrue("Check element contains arquivo.pt reference",
					driver.findElement(By.xpath("/html/body/footer/div/div/div[1]/div[1]/div")).getText()
							.contains("Arquivo.pt"));
		});

	}
}
