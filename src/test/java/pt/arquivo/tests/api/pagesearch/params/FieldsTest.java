package pt.arquivo.tests.api.pagesearch.params;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.junit.Test;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONTokener;

import pt.arquivo.selenium.Retry;
import pt.arquivo.utils.AppendableErrorsBaseTest;

/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class FieldsTest extends AppendableErrorsBaseTest {

	private String testURL;

	public FieldsTest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		
		URL url;
		JSONTokener tokener;
		JSONObject reply;
		JSONArray apiResults;

		String fromAPI = this.testURL + "/textsearch?q=fccn&fields=title,collection";
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

		for(int i=0;i<apiResults.length();i++){
			assertEquals("Verify 'title' is present on the " +String.valueOf(i) + "th reply", true, apiResults.getJSONObject(i).has("title"));
			assertEquals("Verify 'collection' is present on the " +String.valueOf(i) + "th reply", true, apiResults.getJSONObject(i).has("collection"));
			assertEquals("Verify 'tstamp' is NOT present on the " +String.valueOf(i) + "th reply", false, apiResults.getJSONObject(i).has("tstamp"));
			assertEquals("Verify 'digest' NOT is present on the " +String.valueOf(i) + "th reply",false, apiResults.getJSONObject(i).has("digest"));
		}

	}


}
