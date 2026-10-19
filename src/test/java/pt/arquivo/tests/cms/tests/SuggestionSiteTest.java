package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.CollaboratePage;
import pt.arquivo.tests.cms.pages.IndexSobrePage;
import pt.arquivo.tests.cms.pages.SuggestionPage;

public class SuggestionSiteTest extends WebDriverTestBaseParallel{

	public SuggestionSiteTest(Map<String, String> config) {
		super(config);
	}

	/**
	 * Checks that the suggestion form is shown. It isn't submitted, submitting it is a manual test
	 */
	@Test
	@Retry
	public void suggestionsSiteTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			index = new IndexSobrePage( driver );
			CollaboratePage collaborate = index.goToCollaboratePage( "PT" );
			System.out.println( "Going to the Collborate Page" );
		    SuggestionPage sug = collaborate.goToSuggestionSitePage( "PT" );
		    System.out.println( "Going to the Suggestion Site Page" );
	        assertTrue("Failed The Suggestion Site Page Test in Portuguese", sug.checkSuggestionForm( "PT" ) );
	        
	        //TODO english version missing
	        
		} catch( IOException e ) {
			fail("IOException -> suggestionsSiteTest");
		}
		
    }
	
}
