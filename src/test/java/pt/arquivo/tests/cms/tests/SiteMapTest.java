package pt.arquivo.tests.cms.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import pt.arquivo.selenium.Retry;
import pt.arquivo.selenium.WebDriverTestBaseParallel;
import pt.arquivo.tests.cms.pages.IndexSobrePage;
import pt.arquivo.tests.cms.pages.SiteMapPage;

public class SiteMapTest extends WebDriverTestBaseParallel {

	public SiteMapTest(Map<String, String> config) {
		super(config);
	}

	@Test
	@Retry
	public void SiteMTest( )  {
		System.out.print( "Running examples Test. \n");
		IndexSobrePage index = null;
		try{
			index = new IndexSobrePage( driver );
			SiteMapPage siteMap = index.goToSiteMapPage( );
			System.out.println( "Going to the SiteMTest" );
		    
			/**************************/
			/*** Portuguese version ***/ 
			/**************************/
	        assertTrue("Failed The SiteMap Page Test in Portuguese", siteMap.checkSiteMap( "PT" ) );
	        System.out.println( "Success The SiteMap Page Test in Portuguese" );
	        
	        /***********************/
	        /*** English version ***/ 
	        /***********************/
	        assertTrue("Failed The SiteMap Page Test in English", siteMap.checkSiteMap( "EN" ) );
	        System.out.println( "Success The SiteMap Page Test in English" );
	        
		} catch( IOException e ) {
			fail("IOException -> SiteMTest");
		}
    }
	
}
