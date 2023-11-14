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
	public void findArquivoReference() {
		driver.get("http://contamehistorias.pt");

		appendError(() -> {
			assertTrue("Check footer contains arquivo.pt reference",
					driver.findElement(By.xpath("/html/body/footer")).getText()
							.contains("Arquivo.pt"));
		});

	}
}
