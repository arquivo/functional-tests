package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.IndexSobrePage;

public class FooterTest extends WebDriverTestBaseParallel {

	public FooterTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
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
