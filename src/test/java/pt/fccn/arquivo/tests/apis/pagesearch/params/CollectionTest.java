package pt.fccn.arquivo.tests.apis.pagesearch.params;

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
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONTokener;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.util.AppendableErrorsBaseTest;

/**
 * 
 * @author pedro.gomes.fccn@gmail.com
 *
 */

public class CollectionTest extends AppendableErrorsBaseTest {

	private String testURL;

	public CollectionTest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		
		URL url;
		JSONTokener tokener;
		JSONObject reply;
		JSONArray apiResults;
		String collection;

		String fromAPI = this.testURL + "/textsearch?q=fccn&collection=AWP2";
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
			collection = apiResults.getJSONObject(i).getString("collection");
			assertEquals("Verify 'collection' parameter of API " +String.valueOf(i) + "th reply is AWP2", "AWP2", collection);
		}

	}


}
