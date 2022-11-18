package pt.arquivo.tests.cms.pages;

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

public class VideoPage {
	
	WebDriver driver;
	private final int timeout = 50;
	
	public VideoPage( WebDriver driver ) throws FileNotFoundException{
		this.driver = driver;
		
	}
	
	public boolean checkVideoLinks( String language ) {
		System.out.println( "[checkVideoLinks]" );
		String id = "";
		if( language.equals( "PT" ) )
			id = "post-2756";
		else {
			switchLanguage( );
			id = "post-2816";
		}
		String xpathVideos 			= "//*[@id=\"" + id + "\"]/div/div/div/ul/li/a";
		String xpathDissemination 	= "//*[@id=\"" + id + "\"]/div/div/ul[1]/li/a";
    	String xpathTechnical 		= "//*[@id=\"" + id + "\"]/div/div/ul[2]/li/a";

		if( !extractLinks( xpathVideos ) )
			return false;
		if( !extractLinks( xpathDissemination ) )
			return false;
		if( !extractLinks( xpathTechnical ) )
			return false;
		
		return true;
	}
	
	
	public boolean extractLinks( String xpath ) {
		try{
    		List< WebElement > results = ( new WebDriverWait( driver, Duration.ofSeconds(timeout )) )
	                .until( ExpectedConditions
	                        .visibilityOfAllElementsLocatedBy(
	                        		      By.xpath( xpath )
	                        )
	        );
    		
    		System.out.println( "[Video Links] results size = " + results.size( ) );
    		for( WebElement elem : results ) {
    			String url = elem.getAttribute( "href" );
    			int statusCode = AnalyzeURLs.linkExists( url );
    			String text = elem.getText( );
    			Charset.forName( "UTF-8" ).encode( text );
    			if( !AnalyzeURLs.checkOk( statusCode ) ) {
    				System.out.println( "Failed: text["+text+"] link[" + url + "] status-code[" + statusCode + "]" );
    				return false;
    			}
    		}
    		
	    	return true;
    	} catch( NoSuchElementException e ){
            System.out.println( "Error in checkVideoLinks" );
            e.printStackTrace( );
            return false;
    	}
	}

    /**
    * Change to the English version
    */
    private void switchLanguage( ){
    	String xpathEnglishVersion = "//*[@id=\"menu-item-3862-en\"]/a";
    	//TODO //*[@id=\"menu-item-3862-en\"]/a -> new template 
      	if( driver.findElement( By.xpath( xpathEnglishVersion ) ).getText( ).equals( "English" ) ) {
      		System.out.println( "Change language to English" );
      		driver.findElement( By.xpath( xpathEnglishVersion ) ).click( );
      		IndexSobrePage.sleepThread( );
      	}
    }
    
    
}
