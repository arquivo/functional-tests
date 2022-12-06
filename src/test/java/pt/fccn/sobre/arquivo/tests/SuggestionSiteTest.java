package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.CollaboratePage;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;
import pt.fccn.sobre.arquivo.pages.SuggestionPage;

public class SuggestionSiteTest extends WebDriverTestBaseParallel{

	public SuggestionSiteTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	/**
	 * Test has no effect. Turned into a manual test
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
		    SuggestionPage sug = collaborate.goToSuggestionSitePage( );
		    System.out.println( "Going to the Suggestion Site Page" );
	        assertTrue("Failed The Suggestion Site Page Test in Portuguese", sug.sendSuggestion( "PT" ) );
	        
	        //TODO english version missing
	        
		} catch( IOException e ) {
			fail("IOException -> suggestionsSiteTest");
		}
		
    }
	
}
