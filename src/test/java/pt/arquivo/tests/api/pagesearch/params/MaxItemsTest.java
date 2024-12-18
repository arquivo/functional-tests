package pt.arquivo.tests.api.pagesearch.params;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

public class MaxItemsTest extends AppendableErrorsBaseTest {

	private String testURL;

	public MaxItemsTest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		
		URL url;
		JSONTokener tokener;
		JSONObject reply;
		JSONArray apiResults;
		
		//We set dedupValue to 1 becaue deduplication with solr means that repeated results may exceed the maxItems 
		String fromAPI = this.testURL + "/textsearch?q=fccn&maxItems=5&dedupValue=1";
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

		assertEquals("Verify that the API reply has exactly 5 results", 5, apiResults.length() );
	}


}
