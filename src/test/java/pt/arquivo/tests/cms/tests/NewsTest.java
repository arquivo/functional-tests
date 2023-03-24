package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.IndexSobrePage;
import pt.arquivo.tests.cms.pages.NewsPage;

public class NewsTest extends WebDriverTestBaseParallel {

	public NewsTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void newsTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			index = new IndexSobrePage( driver );
			NewsPage news = index.goToNewsPage( );
			System.out.println( "Going to the NewsTest" );
			/**************************/
			/*** Portuguese version ***/ 
			/**************************/
	        assertTrue("Failed The News Page Test in Portuguese", news.checkNewsLinks( "PT" ) );
	        System.out.println( "Success The News Page Test in Portuguese" );
	        
	        /***********************/
	        /*** English version ***/ 
	        /***********************/
	        assertTrue("Failed The News Page Test in English", news.checkNewsLinks( "EN" ) );
	        System.out.println( "Success The News Page Test in English" );
	        
		} catch( IOException e ) {
			fail("IOException -> newsTest");
		}
		
    }
	
}
