package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import pt.fccn.saw.selenium.Retry;
import pt.fccn.saw.selenium.WebDriverTestBaseParalell;
import pt.fccn.sobre.arquivo.pages.CommonQuestionsPage;
import pt.fccn.sobre.arquivo.pages.ExamplesPage;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;

public class ExamplesTest extends WebDriverTestBaseParalell {

	public ExamplesTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super( os, version, browser, deviceName, deviceOrientation );
	}
	
	@Test
	@Retry
	public void examplesTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			 index = new IndexSobrePage( driver );
		} catch( IOException e ) {
			fail("IOException -> IndexSobrePage");
		}
		
        System.out.println( "Going to the CommonQuestions" );
        try{
	        ExamplesPage examplePage = index.goToExamplePage( );
	        assertTrue("Failed The Common Question in Portuguese", examplePage. );

	        assertTrue("Failed The Common Question in English", examplePage. );
        } catch( FileNotFoundException e ) {
			fail("FileNotFoundException -> goToExamplePage");
		}
    }

}