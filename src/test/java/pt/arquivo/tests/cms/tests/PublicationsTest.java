package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.IndexSobrePage;
import pt.arquivo.tests.cms.pages.PublicationsPage;

public class PublicationsTest extends WebDriverTestBaseParallel {

	public PublicationsTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void publicationsTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			index = new IndexSobrePage( driver );
			PublicationsPage publications = index.goToPublicationsPage( "PT" );
			System.out.println( "Going to the PublicationsTest" );
		    
			/**************************/
			/*** Portuguese version ***/ 
			/**************************/
	        assertTrue("Failed The Publications Page Test in Portuguese", publications.checkPubicationsLinks( "PT" ) );
	        System.out.println( "Success The Publications Page Test in Portuguese" );
	        
	        /***********************/
	        /*** English version ***/ 
	        /***********************/
	        assertTrue("Failed The Publications Page Test in Portuguese", publications.checkPubicationsLinks( "EN" ) );
	        System.out.println( "Success The Publications Page Test in English" );
	        
		} catch( IOException e ) {
			fail("IOException -> publicationsTest");
		}
		
    }
}
