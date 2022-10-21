package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;

public class FooterTest extends WebDriverTestBaseParallel {

	public FooterTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}

	@Test
	@Retry
	public void footerTest( ) {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			 index = new IndexSobrePage( driver );
		} catch( IOException e ) {
			fail("IOException -> footerTest");
		}
		
        System.out.println( "Going to the FooterTest" );
        /**************************/
        /*** Portuguese version ***/
        /**************************/
        assertTrue("Failed The Footer Test in Portuguese", index.checkFooterURLs( "PT" ) );
        System.out.println( "Success The Footer Test in Portuguese" );
        
        /***********************/
        /*** English version ***/ 
        /***********************/
        assertTrue("Failed The Footer Test in English", index.checkFooterURLs( "EN" ) );
        System.out.println( "Success The Footer Test in English" );
	   
    }
	
	
	
}
