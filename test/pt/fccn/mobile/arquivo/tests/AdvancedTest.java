package pt.fccn.mobile.arquivo.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.fccn.mobile.arquivo.pages.AdvancedSearchPage;
import pt.fccn.mobile.arquivo.pages.IndexMobilePage;
import pt.fccn.saw.selenium.WebDriverTestBaseParalell;

public class AdvancedTest extends WebDriverTestBaseParalell {

	
	public AdvancedTest( String os, String version, String browser, String deviceName, String deviceOrientation ) {
		super( os, version, browser, deviceName, deviceOrientation );
	}
	
	String term = "Pesquisa Avançada — Arquivo.pt";
    @Test
    public void SearchAdvancedTest( ) {
    	System.out.print( "[Mobile] Running SearchAdvancedTest URL. \n");
		IndexMobilePage index = null;
		index = new IndexMobilePage( driver );
		Ispre_prod = index.setPreProd( pre_prod );
    	AdvancedSearchPage advPage =  index.goToAdvancedPage( );
		/**************************/
		/*** Portuguese version ***/ 
		/**************************/
        assertTrue("Failed The Advanced Search Test in Portuguese", advPage.checkAdvancedSearch( "PT" ) );
        System.out.println( "Success The Advanced Search Test in Portuguese" );
        
        /***********************/
        /*** English version ***/ 
        /***********************/
        assertTrue("Failed The Advanced Search Test in English", advPage.checkAdvancedSearch( "EN" ) );
        System.out.println( "Success The Advanced Search Test in English" );
    }
    
}
