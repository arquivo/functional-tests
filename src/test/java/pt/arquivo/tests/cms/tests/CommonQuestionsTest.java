package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.CommonQuestionsPage;
import pt.arquivo.tests.cms.pages.IndexSobrePage;

public class CommonQuestionsTest extends WebDriverTestBaseParallel {

	public CommonQuestionsTest(String os, String version, String browser, String deviceName, String deviceOrientation, String automationName) {
		super(os, version, browser, deviceName, deviceOrientation, automationName);
	}
	
	boolean isPreProd = true;
	
	@Test
	@Retry
	public void commonQuestionsTest( )  {
		System.out.print( "Running Common Questions Test. \n");
		IndexSobrePage index = null;
		try{
			 index = new IndexSobrePage( driver );
		} catch( IOException e ) {
			fail("IOException -> IndexSobrePage");
		}
		
        System.out.println( "Going to the CommonQuestions" );
        try{
	        
        	CommonQuestionsPage commonQuestions = index.goToCommonQuestionsPage( );
        	/**************************/
        	/*** Portuguese version ***/ 
        	/**************************/
        	assertTrue("Failed The Common Question in Portuguese", commonQuestions.inspectQuestions( "PT" ) );
			System.out.println( "Success The Common Question in Portuguese" );
			
			/***********************/
			/*** English version ***/ 
			/***********************/
	        assertTrue("Failed The Common Question in English", commonQuestions.inspectQuestions( "EN" ) );
	        System.out.println( "Success The Common Question in English" );
	        
        } catch( FileNotFoundException e ) {
			fail("FileNotFoundException -> goToCommonQuestionsPage");
		}
    }
	


}
