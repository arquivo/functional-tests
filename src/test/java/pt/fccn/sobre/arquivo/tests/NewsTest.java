package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;
import pt.fccn.sobre.arquivo.pages.NewsPage;

public class NewsTest extends WebDriverTestBaseParallel {

	public NewsTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super( os, version, browser, deviceName, deviceOrientation );
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
