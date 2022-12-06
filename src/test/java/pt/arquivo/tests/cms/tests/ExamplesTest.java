package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.ExamplesPage;
import pt.arquivo.tests.cms.pages.IndexSobrePage;

public class ExamplesTest extends WebDriverTestBaseParallel {

	public ExamplesTest(String platformName, String platformVersion, String browser, String browserVersion, String deviceName, String deviceOrientation, String automationName, String resolution) {
		super(platformName, platformVersion, browser, browserVersion, deviceName, deviceOrientation, automationName, resolution);
	}
	
	@Test
	@Retry
	public void examplesTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			 index = new IndexSobrePage( driver );
		} catch( IOException e ) {
			fail("IOException -> examplesTest");
		}
		
        System.out.println( "Going to the ExamplePage" );
        /**************************/
        /*** Portuguese version ***/ 
        /**************************/
        ExamplesPage examplePage = index.goToExamplePage( );
        assertTrue("Failed The Example Page Test in Portuguese", examplePage.checkLinksExamples( "PT" ) );
        System.out.println( "Success The Example Page Test in Portuguese" );
        
        /***********************/
        /*** English version ***/ 
        /***********************/
        assertTrue("Failed The Example Page Test in English", examplePage.checkLinksExamples( "EN" ) );
        System.out.println( "Success The Example Page Test in English" );
    }

}
