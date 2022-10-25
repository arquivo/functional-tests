package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.ExamplesPage;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;

public class ExamplesTest extends WebDriverTestBaseParallel {

	public ExamplesTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
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
