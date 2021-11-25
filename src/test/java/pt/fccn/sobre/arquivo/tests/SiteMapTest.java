package pt.fccn.sobre.arquivo.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import pt.fccn.arquivo.selenium.Retry;
import pt.fccn.arquivo.selenium.WebDriverTestBaseParallel;
import pt.fccn.sobre.arquivo.pages.IndexSobrePage;
import pt.fccn.sobre.arquivo.pages.SiteMapPage;

public class SiteMapTest extends WebDriverTestBaseParallel {

	public SiteMapTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super( os, version, browser, deviceName, deviceOrientation );
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
