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

public class TypeTest extends AppendableErrorsBaseTest {

	private String testURL;

	public TypeTest() {
		this.testURL = System.getProperty("test.url");
	}

	@Test
	@Retry
	public void pageSearchAPITest() {
		
		URL url;
		JSONTokener tokener;
		JSONObject reply;
		JSONArray apiResults;
		String mimetype;

		String fromAPI = this.testURL + "/textsearch?q=fccn&type=pdf";
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
			mimetype = apiResults.getJSONObject(i).getString("mimeType");
			assertEquals("Verify 'mimeType' parameter of API " +String.valueOf(i) + "th reply is 'application/pdf'", "application/pdf", mimetype);
		}

	}


}
