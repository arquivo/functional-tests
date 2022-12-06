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

public class NewsOnMediaPage {

	WebDriver driver;
	private final int timeout = 50;
	
	public NewsOnMediaPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
	}
	
	public boolean checkNewsLinks( String language ) {
		System.out.println( "[checkNewsLinks]" );
		String xpathDiv = "";
		if( language.equals( "EN" ) ){
			xpathDiv = "//*[@id=\"post-2812\"]/div/div"; //get links
			//switchLanguage( );
		}
		else
			xpathDiv = "//*[@id=\"post-2635\"]/div/div"; //get links
		
		try{

     		WebElement divTag = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) ) /* Wait Up to 50 seconds should throw RunTimeExcpetion*/
   	                .until(
   	                		ExpectedConditions.presenceOfElementLocated(
   	                				By.xpath( xpathDiv ) ) );
   	        
			//find all the a tags in the div tag
   		   	List< WebElement > allAnchors = divTag.findElements( By.tagName( "a" ) );
       		
    		System.out.println( "[News Links] results size = " + allAnchors.size( ) );
    		for( WebElement elem : allAnchors ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			System.out.println( "Failed: text[" + Charset.forName( "UTF-8" ).encode( elem.getText( ) ) + "] link[" + url + "] status-code[" + statusCode + "]" );
    			/*if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text[" + Charset.forName( "UTF-8" ).encode( elem.getText( ) ) + "] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}*/
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkNewsLinks" );
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
