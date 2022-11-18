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

import pt.arquivo.utils.AnalyzeURLs;

public class PublicationsPage {
	
	
	WebDriver driver;
	private final int timeout = 50;
	
	public PublicationsPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
	}
	
	
	public boolean checkPubicationsLinks( String language ) {
		System.out.println( "[checkPubicationsLinks]" );
		String xpathDiv = "";
		
		if( language.equals( "PT" ) )
			xpathDiv = "//*[@id=\"post-2225\"]/div/div/div";
		else {
			switchLanguage( );
			xpathDiv = "//*[@id=\"post-2814\"]/div/div";
		}

			
		try{
  		
			WebElement divTag = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
   	                .until(
   	                		ExpectedConditions.presenceOfElementLocated(
   	                				By.xpath( xpathDiv ) ) );
   	        
			//find all the a tags in the div tag
   		   	List< WebElement > allAnchors = divTag.findElements( By.tagName( "a" ) );
       		
    		
    		System.out.println( "[Publications] results size = " + allAnchors.size( ) );
    		for( WebElement elem : allAnchors ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			if( statusCode != 200 )
    				//System.out.println( "Failed: text[" + text + "] link[" + url + "] status-code[" + statusCode + "]" );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text[" + text + "] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkOPSite" );
            e.printStackTrace( );
            return false;
    	}
		
	}
	
    /**
    * Change to the English version
    */
    private void switchLanguage( ){ //*[@id="menu-item-4506-en"]/a
    	String xpathEnglishVersion = "//*[@id=\"menu-item-4424-en\"]/a"; //*[@id="menu-item-4424-en"]/a
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }
	
	
}
