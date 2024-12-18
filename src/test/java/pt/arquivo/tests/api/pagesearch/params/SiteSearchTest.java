package pt.arquivo.tests.api.pagesearch.params;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.AppendableErrorsBaseTest;

/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class SiteSearchTest extends AppendableErrorsBaseTest {

	private String testURL;

	public SiteSearchTest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		
		URL url;
		JSONTokener tokener;
		JSONObject reply;
		JSONArray apiResults;
		String originalURL;

		String fromAPI = this.testURL + "/textsearch?q=fccn&siteSearch=www.publico.pt";
		try {
			url = new URL(fromAPI);
		} catch (MalformedURLException e) {
			throw new RuntimeException(
					"Error generating URL to a Page Search API",
					e);
		}

		try{
			tokener = new JSONTokener(url.openStream());
		} catch (IOException e) {
			throw new RuntimeException(
					"Error starting a request to the Page Search API",
					e);
		}
		reply = new JSONObject(tokener);
		apiResults = reply.getJSONArray("response_items");

		assertEquals("Verify that the API reply isn't empty", true, apiResults.length() > 0);

		Pattern pattern = Pattern.compile("^https?:\\/\\/([^\\.]+\\.)?publico\\.pt.*$");

		for(int i=0;i<apiResults.length();i++){
			originalURL = apiResults.getJSONObject(i).getString("originalURL");
			assertEquals("Verify 'originalURL' parameter of API " +String.valueOf(i) + "th reply is from www.publico.pt", true, pattern.matcher(originalURL).find());
		}

	}


}
