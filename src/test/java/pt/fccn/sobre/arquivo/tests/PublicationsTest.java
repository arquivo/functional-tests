package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;
import pt.fccn.sobre.arquivo.pages.PublicationsPage;

public class PublicationsTest extends WebDriverTestBaseParallel {

	public PublicationsTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super( os, version, browser, deviceName, deviceOrientation );
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
