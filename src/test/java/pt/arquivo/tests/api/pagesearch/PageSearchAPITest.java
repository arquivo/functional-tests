package pt.arquivo.tests.api.pagesearch;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.AppendableErrorsBaseTest;

/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class PageSearchAPITest extends AppendableErrorsBaseTest {

	private String testURL;

	public PageSearchAPITest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		String textAPI = this.testURL + "/textextracted?m=http://www.fccn.pt//19961013145650";
		
		URL url;
		try {
			url = new URL(textAPI);
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"Error generating URL to a Page Search API",
					e);
		}
		byte[] textAPIBytes;
		try {
			InputStream is = url.openStream();
			textAPIBytes = IOUtils.toByteArray(is);

		} catch (IOException e) {
			throw new RuntimeException("Error downloading the text", e);
		}

		String timemap = new String(textAPIBytes);
		String checkTest = "Fundação para a Computação Científica Nacional \" A promoção de infraestruturas no domínio da computação científica gerindo-as, nuns casos, e doando-as noutros, é o principal objectivo da Fundação , que actua predominantemente no âmbito das Universidades, Centros e Laboratórios de I&D.\" A Fundação tem a responsabilidade de gerir e desenvolver a RCCN, Rede da Comunidade Científica Nacional, a rede académica portuguesa. RCCN Outras actividades: SCCN | CCCN | ICCN | ACCN Eventos Publicações Serviços disponíveis Servidores WWW das instituições ligadas à RCCN Home Page de Portugal Estatísticas de utilização deste servidor 5685 visitantes desde 95-11-22 Fundação para a Computação Científica Nacional (FCCN) Av. Brasil, 101 1799 Lisboa Codex Tel. +351 1 8481906 Fax. +351 1 8472167 email. info@fccn.pt Este servidor é mantido por: Florentino dos Santos Gameiro - flo@rccn.net Última alteração: Wednesday, 10-Jan-96 18:13:25 CUT Internet URL- http://www.fccn.pt:80/";
		assertEquals("Verify test search API", timemap, checkTest);
		String[] lines = timemap.split(System.getProperty("line.separator"));
	}
}
