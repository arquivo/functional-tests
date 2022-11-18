package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;
import pt.fccn.sobre.arquivo.pages.SearchPage;

public class SearchTest extends WebDriverTestBaseParallel {

	public SearchTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}
	
	@Test
	@Retry
	public void searchingTest( )  {
		System.out.print( "Running SearchTest Test. \n");
		IndexSobrePage index = null;
		try{
			SearchPage search = new SearchPage( driver );
			System.out.println( "Going to the SearchingTest" );
		    
			/**************************/
			/*** Portuguese version ***/ 
			/**************************/
	        assertTrue("Failed The Search Test in Portuguese", search.checkSearch( "PT" ) );
	        System.out.println( "Success The Search Test in Portuguese" );
	        
	        /***********************/
	        /*** English version ***/ 
	        /***********************/
	        assertTrue("Failed The Search Test in English", search.checkSearch( "EN" ) );
	        System.out.println( "Success The Search Test in English" );
	        
		} catch( IOException e ) {
			fail("IOException -> SearchingTest");
		}
		
    }

}
