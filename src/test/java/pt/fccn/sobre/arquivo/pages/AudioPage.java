package pt.fccn.sobre.arquivo.pages;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.List;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import pt.fccn.arquivo.tests.util.AnalyzeURLs;

public class AudioPage {
	
	WebDriver driver;
	private final int timeout = 50;
	
	public AudioPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
	}
	
	public boolean checkAudioLinks( String language ) {
		System.out.println( "[checkAudioLinks]" );
		String xpatha; 
		
		if( language.equals( "PT" ) )
			xpatha = "//*[@id=\"post-3021\"]/div/div/ul/li/a";
		else {
			xpatha = "//*[@id=\"post-3192\"]/div/div/ul/li/a";
			//switchLanguage( );
		}
		
		try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpatha )
	                        )
	        );
    		
    		System.out.println( "[Audio Links] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text[" + text + "] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkAudioLinks" );
            e.printStackTrace( );
            return false;
    	}
		
	}
	
    /**
    * Change to the English version
    */
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a";
    	//TODO //*[@id=\"menu-item-3862-en\"]/a -> new template 
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }
	
}
