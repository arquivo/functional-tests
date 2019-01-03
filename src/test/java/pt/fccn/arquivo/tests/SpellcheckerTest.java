package pt.fccn.arquivo.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.fccn.arquivo.pages.IndexPage;
import pt.fccn.arquivo.pages.SearchPage;
import pt.fccn.saw.selenium.Retry;
import pt.fccn.saw.selenium.WebDriverTestBaseParalell;

public class SpellcheckerTest extends WebDriverTestBaseParalell {

	public SpellcheckerTest(String os, String version, String browser, String deviceName, String deviceOrientation) {
		super(os, version, browser, deviceName, deviceOrientation);
	}

	@Test
    @Retry
    public void spellcheckerTest( ) {
    	System.out.print( "Running SpellcheckerTest. \n" );
        IndexPage index = new IndexPage( driver );
        Ispre_prod = index.setPreProd( pre_prod );
        String term = "recuperação do chiado";
        
        SearchPage searchResults = index.search( term );
        assertTrue("Failed The SpellChecker Test in Portuguese", searchResults.spellcheckerCorrect(  ) );
        System.out.println( "Success The SpellChecker Test in Portuguese" );
        
        index.switchLanguage( );
        searchResults = index.search( term );
        assertTrue("Failed The SpellChecker Test in English", searchResults.spellcheckerCorrect(  ) );
        System.out.println( "Success The SpellChecker Test in English" ); 
    }
	
    
    
}